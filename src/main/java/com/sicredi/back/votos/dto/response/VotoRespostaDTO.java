package com.sicredi.back.votos.dto.response;

import com.sicredi.back.votos.entities.Voto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VotoRespostaDTO {

    private String voto;
    private PautaRespostaDTO pauta;
    private String cpfAssociado;

    public VotoRespostaDTO transformaParaObjeto(Voto voto) {
        this.voto = voto.getVoto();
        this.pauta = new PautaRespostaDTO().transformaParaObjeto(voto.getPauta());
        this.cpfAssociado = voto.getAssociado().getCpf();
        return this;
    }
}
