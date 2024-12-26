package com.kauanmeira.api_contrato.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidarData.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface isDataValida {
    String message() default "Data inválida.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}