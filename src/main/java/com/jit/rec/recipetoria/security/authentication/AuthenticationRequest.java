package com.jit.rec.recipetoria.security.authentication;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @Nullable
    @Size(min = 1, max = 50, message = "{validation.authenticationRequest.name.Size}")
    private String name;

    @Email(message = "{validation.authenticationRequest.email.Email}")
    private String email;

    @Size(min = 3, max = 30, message = "{validation.authenticationRequest.password.Size}")
    private String password;
}
