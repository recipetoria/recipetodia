package com.jit.rec.recipetoria.security.authentication;

import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserRepository;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserRole;
import com.jit.rec.recipetoria.security.configuration.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        ApplicationUser applicationUser = ApplicationUser.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .applicationUserRole(ApplicationUserRole.USER)
                .build();
        applicationUserRepository.save(applicationUser);
        String jwtToken = jwtService.generateToken(applicationUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
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
