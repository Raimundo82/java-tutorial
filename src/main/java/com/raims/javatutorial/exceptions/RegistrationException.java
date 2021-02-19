package com.raims.javatutorial.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrationException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public RegistrationException(ErrorMessage errorMessage, String value) {
        super(String.format(errorMessage.label, value));
        this.errorMessage = errorMessage;
    }

}
