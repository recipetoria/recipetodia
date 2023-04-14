package com.jit.rec.recipetoria.security.authentication;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @NotEmpty(message = "Please provide a valid email address")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Size(min = 3, max = 30, message = "Password must be between 3 and 30 characters")
    private String password;
}
