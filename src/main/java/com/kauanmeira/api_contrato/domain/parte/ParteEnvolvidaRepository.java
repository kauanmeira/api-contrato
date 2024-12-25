package com.kauanmeira.api_contrato.domain.parte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParteEnvolvidaRepository extends JpaRepository<ParteEnvolvida, Long> {
}