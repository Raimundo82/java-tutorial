package com.raims.javatutorial.registration;

import lombok.Data;

@Data
public class RegistrationRequest {
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String email;


}
