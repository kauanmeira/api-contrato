package com.kauanmeira.api_contrato.dto;


import com.kauanmeira.api_contrato.domain.contrato.StatusContrato;
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
public class BuscarContratoDTO implements Serializable {
    private Long numeroContrato;

    private LocalDate dataCriacao;

    private String descricaoContrato;

    private StatusContrato statusContrato;

    private List<ParteEnvolvidaDTO> partesEnvolvidas = new ArrayList<>();
}