package com.raims.javatutorial.appuser;

import com.raims.javatutorial.exceptions.RegistrationException;
import com.raims.javatutorial.registration.token.ConfirmationToken;
import com.raims.javatutorial.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.raims.javatutorial.exceptions.ErrorMessage.EMAIL_ALREADY_REGISTERED;
import static com.raims.javatutorial.exceptions.ErrorMessage.USER_NOT_FOUND;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RegistrationException(USER_NOT_FOUND, email));
    }

    public String signUpUser(AppUser appUser) {
        boolean emailExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if (emailExists)
            throw new RegistrationException(
                    EMAIL_ALREADY_REGISTERED, appUser.getEmail()
            );
        appUser.setPassword(bcryptPasswordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);
        ConfirmationToken Confirmationtoken = new ConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15L),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(Confirmationtoken);

        // TODO : Send email

        return Confirmationtoken.getToken();
    }


    @Transactional
    public void enableAppUser(AppUser appUser) {
        appUser.setEnabled(true);
    }

}
