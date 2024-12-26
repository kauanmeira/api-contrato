package com.kauanmeira.api_contrato.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class ValidarData implements ConstraintValidator<isDataValida, LocalDate> {

    @Override
    public boolean isValid(LocalDate data, ConstraintValidatorContext context) {
        if (data == null) {
            return true;
        }

        LocalDate hoje = LocalDate.now();
        LocalDate limiteInferior = LocalDate.of(1900, 1, 1);

        return !data.isAfter(hoje) && !data.isBefore(limiteInferior);
    }
}