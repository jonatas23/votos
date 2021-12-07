package com.sicredi.back.votos.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String voto;

    @ManyToOne(cascade = CascadeType.ALL)
    private Pauta pauta;

    @ManyToOne(cascade = CascadeType.ALL)
    private Associado associado;

}
