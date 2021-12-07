package com.sicredi.back.votos.controller;

import com.sicredi.back.votos.dto.PautaDTO;
import com.sicredi.back.votos.dto.SessaoDTO;
import com.sicredi.back.votos.dto.response.PautaRespostaDTO;
import com.sicredi.back.votos.entities.Pauta;
import com.sicredi.back.votos.service.PautaService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pautas")
public class PautasController {

    @Autowired
    PautaService pautaService;

    @Autowired
    ModelMapper modelMapper;

    @ApiOperation(value = "Está operação lista todas as Pautas Cadastradas.")
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<Pauta>> pautas(){
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarTodos());
    }

    @ApiOperation(value = "Está operação salva uma nova Pauta.")
    @PostMapping(value = "/")
    public ResponseEntity<PautaRespostaDTO> salvar(@RequestBody PautaDTO pautaDTO){
        Pauta pauta = pautaService.salvar(this.modelMapper.map(pautaDTO, Pauta.class));
        return ResponseEntity.status(HttpStatus.OK).body(this.modelMapper.map(pauta, PautaRespostaDTO.class));
    }

    @ApiOperation(value = "Está operação criara uma nova Sessão para Pauta.")
    @PostMapping(value = "/abrirSessao")
    public ResponseEntity<Pauta> abrirSessao(@RequestBody SessaoDTO sessaoDTO) {
        Pauta pauta = pautaService.abrirSessao(sessaoDTO);
        return ResponseEntity.status(HttpStatus.OK).body(pauta);
    }

}
