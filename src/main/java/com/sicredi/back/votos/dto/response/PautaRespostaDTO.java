package com.sicredi.back.votos.dto.response;

import com.sicredi.back.votos.entities.Pauta;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
public class PautaRespostaDTO {

    private Long id;
    private String nome;
    private String descricao;

    public PautaRespostaDTO transformaParaObjeto(Pauta pauta) {
        return new ModelMapper().map(pauta, PautaRespostaDTO.class);
    }

}
