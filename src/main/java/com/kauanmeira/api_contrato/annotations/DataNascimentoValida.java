package com.kauanmeira.api_contrato.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidarDataNascimento.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataNascimentoValida {
    String message() default "Data de nascimento inválida.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}