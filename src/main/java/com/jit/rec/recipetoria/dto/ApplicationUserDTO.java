package com.jit.rec.recipetoria.dto;

import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public record ApplicationUserDTO(

        @Nullable
        @Email(message = "Please provide a valid email address")
        String email,

        @Nullable
        @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
        String name,

        @Nullable
        String photo,

        @Nullable
        @Size(min = 3, max = 30, message = "Password must be between 3 and 30 characters")
        String password

) {
    public static ApplicationUserDTO convertToDTO(ApplicationUser applicationUser) {
        return new ApplicationUserDTO(
                applicationUser.getEmail(),
                applicationUser.getName(),
                applicationUser.getPhoto(),
                null
        );
    }
}
