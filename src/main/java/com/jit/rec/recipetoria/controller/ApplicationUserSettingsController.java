package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.ApplicationUserDTO;
import com.jit.rec.recipetoria.service.ApplicationUserSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/client/settings")
@RequiredArgsConstructor
public class ApplicationUserSettingsController {

    private final ApplicationUserSettingsService applicationUserSettingsService;

    @GetMapping
    public ResponseEntity<ApplicationUserDTO> showSettings() {
        ApplicationUserDTO applicationUserDTO = applicationUserSettingsService.getApplicationUser();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(applicationUserDTO);
    }

    @PatchMapping("/personal-info")
    public ResponseEntity<ApplicationUserDTO> updateApplicationUserInfo(
            @Valid @RequestBody ApplicationUserDTO applicationUserInfo) {
        ApplicationUserDTO updatedApplicationUserDTO =
                applicationUserSettingsService.updatePersonalInfo(applicationUserInfo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedApplicationUserDTO);
    }

    @PatchMapping("/photo")
    public ResponseEntity<ApplicationUserDTO> updateApplicationUserPhoto(@RequestBody MultipartFile file) throws IOException {
        ApplicationUserDTO updatedApplicationUserDTO = applicationUserSettingsService.updatePhoto(file);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedApplicationUserDTO);
    }

    @DeleteMapping("/photo-delete")
    public ResponseEntity<ApplicationUserDTO> deleteApplicationUserPhoto() throws IOException {
        ApplicationUserDTO updatedApplicationUserDTO = applicationUserSettingsService.deletePhoto();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedApplicationUserDTO);
    }

    @PatchMapping("/password")
    public ResponseEntity<ApplicationUserDTO> updateApplicationUserPassword(
            @Valid @RequestBody ApplicationUserDTO applicationUserInfo) {
        ApplicationUserDTO updatedApplicationUserDTO = applicationUserSettingsService.updatePassword(applicationUserInfo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedApplicationUserDTO);
    }

    @DeleteMapping("/account-delete")
    public ResponseEntity<String> deleteApplicationUser() {
        applicationUserSettingsService.deleteApplicationUser();

        String message = "Account has been deleted!";

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(message);
    }
}
