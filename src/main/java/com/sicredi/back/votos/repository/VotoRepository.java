package com.sicredi.back.votos.repository;

import com.sicredi.back.votos.entities.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    @Query("SELECT v from Voto v where v.pauta.id = :idPauta")
    public List<Voto> findSessao(@Param("idPauta") Long idPauta);
}
