package com.kauanmeira.api_contrato.domain.contrato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    Optional<Contrato> findContratoByNumeroContrato(Long numero);
    List<Contrato> findContratoByDataCriacao(LocalDate dataCriacao);

    List<Contrato> findAllByNumeroContratoIn(List<Long> numerosContratos);

    List<Contrato> findByPartesEnvolvidas_InscricaoFederal(String inscricaoFederal);
}