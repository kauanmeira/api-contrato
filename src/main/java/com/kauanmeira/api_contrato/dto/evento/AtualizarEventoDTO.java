package com.kauanmeira.api_contrato.dto.evento;

import com.kauanmeira.api_contrato.annotations.isDataValida;
import com.kauanmeira.api_contrato.domain.evento.TipoEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarEventoDTO {
    private TipoEvento tipoEvento;

    @isDataValida
    private LocalDate dataRegistro;

    private String descricaoEvento;
}
