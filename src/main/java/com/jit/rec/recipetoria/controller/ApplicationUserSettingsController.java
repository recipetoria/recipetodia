package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.security.authentication.RegistrationRequest;
import com.jit.rec.recipetoria.service.ApplicationUserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/client/settings")
@RequiredArgsConstructor
public class ApplicationUserSettingsController {

    private final ApplicationUserSettingsService applicationUserSettingsService;

    @GetMapping
    public ApplicationUser showSettings() {
        return applicationUserSettingsService.getApplicationUser();
    }

    @PatchMapping("/personal-info")
    public void updateApplicationUserInfo(@RequestBody RegistrationRequest registrationRequest) {
        applicationUserSettingsService.updatePersonalInfo(registrationRequest);
    }

    @PatchMapping("/password")
    public void updateApplicationUserPassword(@RequestBody RegistrationRequest registrationRequest) {
        applicationUserSettingsService.updatePassword(registrationRequest.getPassword());
    }

    @PatchMapping("/photo")
    public void updateApplicationUserPhoto(@RequestBody MultipartFile file) throws IOException {
        applicationUserSettingsService.updatePhoto(file);
    }

    @DeleteMapping("/photo-delete")
    public void deleteApplicationUserPhoto() throws IOException {
        applicationUserSettingsService.deletePhoto();
    }

    @DeleteMapping("/account-delete")
    public void deleteApplicationUser() {
        applicationUserSettingsService.deleteApplicationUser();
    }
}
