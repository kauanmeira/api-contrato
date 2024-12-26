package com.kauanmeira.api_contrato.domain.evento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByContrato_NumeroContrato(Long numeroContrato);

    Optional<Evento> findByContrato_NumeroContratoAndTipoEvento(Long numeroContrato, TipoEvento tipoEvento);
}