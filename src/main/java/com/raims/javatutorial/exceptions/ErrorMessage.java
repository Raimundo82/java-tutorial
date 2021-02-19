package com.raims.javatutorial.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorMessage {
    USER_NOT_FOUND("User with email %s not found"),
    EMAIL_ALREADY_REGISTERED("email %s already registered"),
    EMAIL_NOT_VALID("email %s is not valid"),
    TOKEN_NOT_FOUND("token %s not found"),
    EMAIL_ALREADY_CONFIRMED("Email %s already confirmed"),
    TOKEN_EXPIRED("Token %s has already expired");

    public final String label;
}
