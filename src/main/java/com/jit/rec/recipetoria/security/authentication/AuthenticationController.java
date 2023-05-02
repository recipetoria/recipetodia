package com.jit.rec.recipetoria.security.authentication;

import com.jit.rec.recipetoria.entity.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws IOException {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User registered successfully")
                        .data(Map.of("authenticationResponse",
                                authenticationService.register(authenticationRequest)))
                        .build()
        );
    }

    @GetMapping("/verify-email") //TODO: check exceptions
    public ResponseEntity<Response> verifyEmail(@RequestParam("token") String verificationToken) {
        authenticationService.verifyEmail(verificationToken);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User verified successfully")
                        .build()
        );
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User authenticated successfully")
                        .data(Map.of("authenticationResponse",
                                authenticationService.authenticate(authenticationRequest)))
                        .build()
        );
    }
}
