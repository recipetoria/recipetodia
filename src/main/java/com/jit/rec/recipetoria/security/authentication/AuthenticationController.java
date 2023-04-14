package com.jit.rec.recipetoria.security.authentication;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.register(authenticationRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationResponse);
    }

    @GetMapping("/verify-email") //TODO: check exceptions
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String verificationToken) {
        authenticationService.verifyEmail(verificationToken);

        String message = "User verified successfully.";

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(message);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationResponse);
    }
}
