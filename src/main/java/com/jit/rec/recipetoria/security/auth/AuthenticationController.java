package com.jit.rec.recipetoria.security.auth;

import com.jit.rec.recipetoria.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final MessageSource messageSource;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .message(messageSource.getMessage(
                                "response.authentication.register", null, Locale.getDefault()))
                        .data(Map.of("authenticationResponse",
                                authenticationService.register(authenticationRequest)))
                        .build()
                );
    }

    @GetMapping("/verify-email") //TODO: check exceptions
    public ResponseEntity<Response> verifyEmail(@RequestParam("token") String verificationToken) {
        authenticationService.verifyEmail(verificationToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.authentication.verifyEmail", null, Locale.getDefault()))
                        .build()
                );
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.authentication.authenticate", null, Locale.getDefault()))
                        .data(Map.of("authenticationResponse",
                                authenticationService.authenticate(authenticationRequest)))
                        .build()
                );
    }
}
