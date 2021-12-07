package com.sicredi.back.votos.controller;

import com.sicredi.back.votos.dto.AssociadoDTO;
import com.sicredi.back.votos.entities.Associado;
import com.sicredi.back.votos.service.AssociadoService;
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
@RequestMapping("/api/associados")
public class AssociadosController {

    @Autowired
    AssociadoService associadoService;

    @Autowired
    ModelMapper modelMapper;

    @ApiOperation(value = "Está operação salva um novo Associado.")
    @PostMapping(value = "/")
    public ResponseEntity<Associado> salvar(@RequestBody AssociadoDTO associadoDTO){
        Associado associado = associadoService.salvar(this.modelMapper.map(associadoDTO, Associado.class));
        return ResponseEntity.status(HttpStatus.OK).body(associado);
    }
}
