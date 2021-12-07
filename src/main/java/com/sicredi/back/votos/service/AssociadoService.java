package com.sicredi.back.votos.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sicredi.back.votos.dto.UserDTO;
import com.sicredi.back.votos.entities.Associado;
import com.sicredi.back.votos.exception.MensagemException;
import com.sicredi.back.votos.exception.NotFoundException;
import com.sicredi.back.votos.exception.ServerException;
import com.sicredi.back.votos.repository.AssociadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AssociadoService {

    AssociadoRepository associadoRepository;
    WebClient webClient;

    @Autowired
    public AssociadoService(AssociadoRepository associadoRepository, WebClient webClient) {
        this.associadoRepository = associadoRepository;
        this.webClient = webClient;
    }

    public Associado salvar(Associado associado) throws MensagemException {
        try {
            this.validarAssosiado(associado);
            return associadoRepository.save(associado);
        } catch (MensagemException e) {
            throw new MensagemException(e.getMessage());
        }
    }

    public Associado buscarCpf(String cpf) {
        return associadoRepository.buscarCpf(cpf);
    }

    private void validarAssosiado(Associado associado) throws MensagemException {
        if (!associado.camposObrigatorios()) {
            throw new MensagemException("Nome e CPF são obrigatorios!");
        }

        if (!this.validarCpf(associado.getCpf())) {
            throw new MensagemException("CPF não esta autorizado a executar está ação!");
        }

        if (associadoRepository.buscarCpf(associado.getCpf()) != null) {
            throw new MensagemException("CPF já cadastrado!");
        }
    }

    public boolean validarCpf(String cpf) {
        UserDTO userDTO =  webClient
                .get()
                .uri("https://user-info.herokuapp.com/users/{cpf}", cpf)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new NotFoundException("API not found")))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new ServerException("Server is not responding")))
                .bodyToMono(UserDTO.class)
                .block();

        if ("ABLE_TO_VOTE".equals(userDTO.getStatus()))
            return true;
        else if ("UNABLE_TO_VOTE".equals(userDTO.getStatus()))
            return false;

        return false;
    }
}
