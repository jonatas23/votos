package com.sicredi.back.votos.service;

import com.sicredi.back.votos.entities.Associado;
import com.sicredi.back.votos.entities.Pauta;
import com.sicredi.back.votos.entities.Voto;
import com.sicredi.back.votos.exception.MensagemException;
import com.sicredi.back.votos.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class VotoService {

    VotoRepository votoRepository;
    AssociadoService associadoService;
    PautaService pautaService;

    @Autowired
    public VotoService(VotoRepository votoRepository, @Lazy PautaService pautaService, AssociadoService associadoService) {
        this.votoRepository = votoRepository;
        this.pautaService = pautaService;
        this.associadoService = associadoService;
    }

    public Voto salvar(Voto voto) throws MensagemException {
        try {
            Associado associado = this.validarAssociado(voto.getAssociado().getCpf());
            Pauta pauta = pautaService.validarSessao(voto.getPauta().getId());

            if (this.buscarSessao(voto.getPauta().getId()).stream().anyMatch(v -> v.getAssociado().equals(associado))){
                throw new MensagemException("Associado já votou nesta sessão!");
            }

            if (!(voto.getVoto().equalsIgnoreCase("SIM") || voto.getVoto().equalsIgnoreCase("NÃO") || voto.getVoto().equalsIgnoreCase("NAO"))) {
                throw new MensagemException("Os votos são apenas 'Sim'/'Não'!");
            }

            voto.setPauta(pauta);
            voto.setAssociado(associado);

            return votoRepository.save(voto);
        } catch (Exception e) {
            throw new MensagemException(e.getMessage());
        }
    }

    public List<Voto> buscarSessao(Long idSessao) {
        return votoRepository.findSessao(idSessao);
    }

    private Associado validarAssociado(String cpf) throws MensagemException {
        Associado associado = associadoService.buscarCpf(cpf);

        if (associado == null) {
            throw new MensagemException("CPF não cadastrado!");
        }

        if (!associadoService.validarCpf(cpf)) {
            throw new MensagemException("Associado não esta autorizado a executar tal ação!");
        }

        return associado;
    }

}
