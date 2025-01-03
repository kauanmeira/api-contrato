package com.kauanmeira.api_contrato.dto.parte;

import com.kauanmeira.api_contrato.annotations.DataNascimentoValida;
import com.kauanmeira.api_contrato.domain.parte.TipoParte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParteEnvolvidaDTO implements Serializable {

    @NotBlank(message = "Informe a inscrição federal (CPF/CNPJ).")
    private String inscricaoFederal;

    @NotBlank(message = "Informe o nome completo.")
    private String nomeCompleto;

    @DataNascimentoValida
    @NotNull(message = "Informe a data de nascimento.")
    private LocalDate dataNascimento;

    @NotNull(message = "Informe o tipo de parte.")
    private TipoParte tipoParte;

    @NotBlank(message = "Informe o telefone.")
    private String telefone;

    @NotBlank(message = "Informe o email.")
    private String email;
}
