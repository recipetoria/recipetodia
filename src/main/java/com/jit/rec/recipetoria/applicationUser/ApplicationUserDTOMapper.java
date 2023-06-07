package com.jit.rec.recipetoria.applicationUser;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ApplicationUserDTOMapper implements Function<ApplicationUser, ApplicationUserDTO> {

    @Override
    public ApplicationUserDTO apply(ApplicationUser applicationUser) {
        return new ApplicationUserDTO(
                applicationUser.getEmail(),
                applicationUser.getName(),
                applicationUser.getProfilePhoto(),
                null
        );
    }
}

