package com.raims.javatutorial.registration.token;

import com.raims.javatutorial.exceptions.RegistrationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.raims.javatutorial.exceptions.ErrorMessage.TOKEN_NOT_FOUND;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).orElseThrow(
                () -> new RegistrationException(TOKEN_NOT_FOUND, token)
        );
        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }
}
