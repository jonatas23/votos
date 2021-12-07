package com.sicredi.back.votos.consumer;

import com.sicredi.back.votos.entities.Pauta;
import com.sicredi.back.votos.repository.PautaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PautaListener {

    @Autowired
    PautaRepository pautaRepository;

    @KafkaListener(topics = "${topic.name.consumer}", groupId = "group_id")
    public void listener(Pauta pauta) {
        pautaRepository.save(pauta);

        log.info("Mensagem recebida: " + pauta);
    }
}
