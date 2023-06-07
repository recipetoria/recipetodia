package com.jit.rec.recipetoria.security.auth;

import com.jit.rec.recipetoria.exception.EmailAlreadyExistsException;
import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserRepository;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserRole;
import com.jit.rec.recipetoria.security.jwt.JwtService;
import com.jit.rec.recipetoria.applicationUser.settings.ApplicationUserSettingsService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserSettingsService applicationUserSettingsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;

    public AuthenticationResponse register(AuthenticationRequest authenticationRequest) {
        checkIfEmailExists(authenticationRequest.email());

        ApplicationUser applicationUser = ApplicationUser.builder()
                .name(authenticationRequest.name())
                .email(authenticationRequest.email())
                .password(passwordEncoder.encode(authenticationRequest.password()))
                .applicationUserRole(ApplicationUserRole.USER)
//                .locked(true) //TODO: uncomment
                .build();

        String jwtToken = jwtService.generateToken(applicationUser);

//        sendEmail(applicationUser, jwtToken); //TODO: uncomment

        applicationUserRepository.save(applicationUser);

        applicationUserSettingsService.updateProfilePhoto(applicationUser);

        applicationUserRepository.save(applicationUser);

        return new AuthenticationResponse(jwtToken);
    }

    private void checkIfEmailExists(String email) {
        Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findByEmail(email);

        if (applicationUserOptional.isPresent()) {
            throw new EmailAlreadyExistsException(messageSource.getMessage(
                    "exception.authentication.emailAlreadyExists", null, Locale.getDefault()));
        }
    }

    public void sendEmail(ApplicationUser applicationUser, String jwtToken) {
        String verificationLink = "http://localhost:8080/api/v1/auth/verify-email?token=" + jwtToken; // TODO: generalize the link
        String subject =
                messageSource.getMessage("message.authentication.emailSubject", null, Locale.getDefault());
        String text =
                messageSource.getMessage("message.authentication.emailText", null, Locale.getDefault())
                        + "\n\n" + verificationLink;
        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(applicationUser.getEmail());
        email.setSubject(subject);
        email.setText(text);

        javaMailSender.send(email);
    }

    public void verifyEmail(String verificationToken) {
        String email = jwtService.verifyEmail(verificationToken);

        ApplicationUser applicationUser = applicationUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(
                        "exception.authentication.emailNotFound", null, Locale.getDefault())));

        applicationUser.setLocked(false);

        applicationUserRepository.save(applicationUser);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.email(),
                        authenticationRequest.password()
                )
        );

        ApplicationUser applicationUser = applicationUserRepository.findByEmail(authenticationRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(
                        "exception.authentication.emailNotFound", null, Locale.getDefault())));

        String jwtToken = jwtService.generateToken(applicationUser);

        return new AuthenticationResponse(jwtToken);
    }
}
