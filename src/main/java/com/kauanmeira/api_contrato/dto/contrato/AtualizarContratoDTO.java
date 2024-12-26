package com.kauanmeira.api_contrato.dto.contrato;

import com.kauanmeira.api_contrato.annotations.isDataValida;
import com.kauanmeira.api_contrato.domain.contrato.StatusContrato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarContratoDTO {
    @isDataValida
    private LocalDate dataCriacao;

    private String descricaoContrato;

    private StatusContrato statusContrato;

    private boolean arquivado;

}
