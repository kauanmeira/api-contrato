package com.kauanmeira.api_contrato.dto;

import com.kauanmeira.api_contrato.domain.contrato.StatusContrato;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvida;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarContratoDTO {

    private LocalDate dataCriacao;

    private String descricaoContrato;

    private StatusContrato statusContrato;

    private List<ParteEnvolvida> partesEnvolvidas = new ArrayList<>();
}
