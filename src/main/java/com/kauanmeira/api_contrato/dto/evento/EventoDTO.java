package com.kauanmeira.api_contrato.dto.evento;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kauanmeira.api_contrato.annotations.isDataValida;
import com.kauanmeira.api_contrato.domain.evento.TipoEvento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {
    @NotNull(message = "Informe o tipo do evento")
    private TipoEvento tipoEvento;

    @isDataValida
    @NotNull(message = "Informe a data do registro.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataRegistro;

    @NotBlank(message = "Informe a descrição do evento.")
    private String descricaoEvento;

    @NotNull(message = "Informe o número do contrato.")
    private Long numeroContrato;
}
