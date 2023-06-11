package com.jit.rec.recipetoria.security.auth;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @Nullable
        @Size(min = 1, max = 50, message = "{validation.authenticationRequest.name.Size}")
        String name,

        @Email(message = "{validation.authenticationRequest.email.Email}")
        String email,

        @Size(min = 3, max = 30, message = "{validation.authenticationRequest.password.Size}")
        String password
) {
}