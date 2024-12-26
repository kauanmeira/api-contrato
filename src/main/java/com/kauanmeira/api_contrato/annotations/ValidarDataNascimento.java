package com.kauanmeira.api_contrato.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class ValidarDataNascimento implements ConstraintValidator<DataNascimentoValida, LocalDate> {

    @Override
    public boolean isValid(LocalDate dataNascimento, ConstraintValidatorContext context) {
        if (dataNascimento == null) {
            return true;
        }

        LocalDate hoje = LocalDate.now();

        if (dataNascimento.isAfter(hoje)) {
            return false;
        }

        int idade = Period.between(dataNascimento, hoje).getYears();
        return idade >= 18 && idade <= 150;
    }
}