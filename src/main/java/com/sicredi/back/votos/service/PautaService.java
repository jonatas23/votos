package com.sicredi.back.votos.service;

import com.sicredi.back.votos.dto.SessaoDTO;
import com.sicredi.back.votos.entities.Pauta;
import com.sicredi.back.votos.entities.Voto;
import com.sicredi.back.votos.exception.MensagemException;
import com.sicredi.back.votos.exception.NotFoundException;
import com.sicredi.back.votos.exception.ServerException;
import com.sicredi.back.votos.repository.PautaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PautaService {

    PautaRepository pautaRepository;
    VotoService votoService;

    @Autowired
    KafkaTemplate<String, Pauta> kafkaTemplate;

    @Value("${topic.name.producer}")
    private String TOPIC;


//    private static final String TOPIC = "topico.teste";

    @Autowired
    public PautaService(PautaRepository pautaRepository, VotoService votoService) {
        this.pautaRepository = pautaRepository;
        this.votoService = votoService;
    }

    public Pauta salvar(Pauta pauta) throws MensagemException {
        if (pauta.getNome() == null || pauta.getNome().equals("")) {
            throw new MensagemException("Pauta vazia");
        }
        return pautaRepository.save(pauta);
    }

    public List<Pauta> buscarTodos() {
        return pautaRepository.findAll();
    }

    @Transactional(rollbackOn = Exception.class)
    public Pauta abrirSessao(SessaoDTO sessaoDTO) throws MensagemException {
        Pauta pauta = pautaRepository.findById(sessaoDTO.getIdPauta()).orElseThrow(() -> new NotFoundException(sessaoDTO.getIdPauta()));

        if (pauta == null) {
            throw new MensagemException("Necessario informar uma Pauta existente!");
        } else if (pauta.getSessaoAberta() != null) {
            throw new MensagemException("Sessão já foi aberta para esta Pauta!");
        }

        pauta.setSessaoAberta(true);
        pauta.setTempoMinutos(sessaoDTO.getTempoMinutos() == 0 ? 1 : sessaoDTO.getTempoMinutos());

        pauta = this.pautaRepository.save(pauta);

        this.encerrarSessao(pauta);

        return pauta;
    }

    private void encerrarSessao(Pauta pauta) {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            List<Voto> votos = votoService.buscarSessao(pauta.getId());

            pauta.setSessaoAberta(!pauta.getSessaoAberta());
            pauta.setTotalVotosSim(votos.stream().filter(v -> v.getVoto().equalsIgnoreCase("SIM")).count());
            pauta.setTotalVotosNao(votos.stream().filter(v -> v.getVoto().equalsIgnoreCase("NÃO") || v.getVoto().equalsIgnoreCase("NAO")).count());

            try {
                log.info("Pauta enviado: {}", pauta);
                kafkaTemplate.send(TOPIC, pauta);
            } catch (Exception e) {
                throw new ServerException("Falha ao registrar topic: " + e.getMessage());
            }
        };

        log.info("Scheduled registrado: {}", pauta);
        ses.schedule(task, pauta.getTempoMinutos(), TimeUnit.MINUTES);
        ses.shutdown();
    }

    public Pauta validarSessao(Long idPauta) throws MensagemException {
        Pauta pauta = pautaRepository.findById(idPauta).orElseThrow(() -> new NotFoundException(idPauta));

        if (!Boolean.TRUE.equals(pauta.getSessaoAberta())) {
            throw new MensagemException("Sessão fechada!");
        }

        return pauta;
    }

}
