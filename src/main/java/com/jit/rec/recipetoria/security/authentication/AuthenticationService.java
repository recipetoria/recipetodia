package com.jit.rec.recipetoria.security.authentication;

import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserRepository;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserRole;
import com.jit.rec.recipetoria.security.configuration.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender javaMailSender;

    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        ApplicationUser applicationUser = ApplicationUser.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .applicationUserRole(ApplicationUserRole.USER)
                .locked(true)
                .build();

        String jwtToken = jwtService.generateToken(applicationUser);

        sendEmail(applicationUser, jwtToken);

        applicationUserRepository.save(applicationUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void sendEmail(ApplicationUser applicationUser, String jwtToken) {
        String verificationLink = "http://localhost:8080/api/v1/auth/verify-email?token=" + jwtToken;

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(applicationUser.getEmail());
        email.setSubject("Verify your email address");
        email.setText("Please verify your email by clicking on the link below:\n\n" + verificationLink);

        javaMailSender.send(email);
    }

    public void verifyEmail(String verificationToken) {
        String email = jwtService.verifyEmail(verificationToken);

        ApplicationUser applicationUser = applicationUserRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email not found!"));

        applicationUser.setLocked(false);

        applicationUserRepository.save(applicationUser);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        ApplicationUser applicationUser = applicationUserRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));

        String jwtToken = jwtService.generateToken(applicationUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
