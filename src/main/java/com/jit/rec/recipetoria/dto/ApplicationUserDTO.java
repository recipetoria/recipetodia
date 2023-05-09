package com.jit.rec.recipetoria.dto;

import com.jit.rec.recipetoria.entity.ApplicationUser;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public record ApplicationUserDTO(
        @Nullable
        @Email(message = "{validation.applicationUserDTO.email.Email}")
        String email,

        @Nullable
        @Size(min = 1, max = 50, message = "{validation.applicationUserDTO.name.Size}")
        String name,

        @Nullable
        String photo,

        @Nullable
        @Size(min = 3, max = 30, message = "{validation.applicationUserDTO.password.Size}")
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
