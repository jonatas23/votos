package com.sicredi.back.votos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PautaDTO {

    private Long id;
    private String nome;
    private String descricao;

}
