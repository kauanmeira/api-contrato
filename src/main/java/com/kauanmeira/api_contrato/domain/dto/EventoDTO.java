package com.kauanmeira.api_contrato.domain.dto;

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

    @NotNull(message = "Informe a data do registro.")
    private LocalDate dataRegistro;

    @NotBlank(message = "Informe a descrição do evento.")
    private String descricaoEvento;
}
