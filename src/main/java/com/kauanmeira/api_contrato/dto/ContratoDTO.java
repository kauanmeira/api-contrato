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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContratoDTO implements Serializable {

    @NotNull(message = "Informe a data de criação.")
    private LocalDate dataCriacao;

    @NotBlank(message = "Informe a descrição do contrato")
    private String descricaoContrato;

    @NotNull(message = "Informe o status do contrato")
    private StatusContrato statusContrato;

    @Valid
    @NotEmpty(message = "Informe as partes envolvidas")
    private List<ParteEnvolvidaDTO> partesEnvolvidas = new ArrayList<>();
}