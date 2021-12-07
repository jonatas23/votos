package com.sicredi.back.votos.controller;

import com.sicredi.back.votos.dto.VotoDTO;
import com.sicredi.back.votos.dto.response.VotoRespostaDTO;
import com.sicredi.back.votos.entities.Voto;
import com.sicredi.back.votos.service.VotoService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votacao")
public class VotosController {

    @Autowired
    VotoService votoService;

    @Autowired
    ModelMapper modelMapper;

    @ApiOperation(value = "Está operação registrara o voto do associado.")
    @PostMapping(value = "/")
    public ResponseEntity<VotoRespostaDTO> salvar(@RequestBody VotoDTO votoDTO) {
        Voto voto = votoService.salvar(this.modelMapper.map(votoDTO, Voto.class));
        return ResponseEntity.status(HttpStatus.OK).body(this.modelMapper.map(voto, VotoRespostaDTO.class));
    }
}
