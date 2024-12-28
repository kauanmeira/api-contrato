package com.kauanmeira.api_contrato.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttusException extends RuntimeException {
    private final HttpStatusCode code;
    private final String message;

    public AttusException(HttpStatusCode code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "AttusException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}