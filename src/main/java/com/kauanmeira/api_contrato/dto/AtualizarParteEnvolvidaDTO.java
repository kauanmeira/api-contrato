package com.kauanmeira.api_contrato.dto;

import com.kauanmeira.api_contrato.domain.parte.TipoParte;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarParteEnvolvidaDTO {

    private String inscricaoFederal;

    private String nomeCompleto;

    private LocalDate dataNascimento;

    private TipoParte tipoParte;

    private String telefone;

    @Email
    private String email;
}
