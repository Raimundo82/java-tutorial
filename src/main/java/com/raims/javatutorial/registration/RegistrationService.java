package com.raims.javatutorial.registration;

import com.raims.javatutorial.appuser.AppUser;
import com.raims.javatutorial.appuser.AppUserRole;
import com.raims.javatutorial.appuser.AppUserService;
import com.raims.javatutorial.exceptions.RegistrationException;
import com.raims.javatutorial.registration.token.ConfirmationToken;
import com.raims.javatutorial.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.raims.javatutorial.exceptions.ErrorMessage.*;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;


    public String register(RegistrationRequest request) {
        if (!emailValidator.test(request.getEmail()))
            throw new RegistrationException(EMAIL_NOT_VALID, request.getEmail());
        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.
                getToken(token).orElseThrow(
                () -> new RegistrationException(
                        TOKEN_NOT_FOUND,
                        token
                ));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new RegistrationException(
                    EMAIL_ALREADY_CONFIRMED,
                    confirmationToken.getAppUser().getEmail()
            );
        }

        if (LocalDateTime.now().isAfter(confirmationToken.getExpiresAt())) {
            throw new RegistrationException(
                    TOKEN_EXPIRED,
                    token
            );
        }
        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUser());

        return "Confirmed";

    }
}
