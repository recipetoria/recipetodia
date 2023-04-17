package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.ApplicationUserDTO;
import com.jit.rec.recipetoria.entity.ApiResponse;
import com.jit.rec.recipetoria.service.ApplicationUserSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/settings")
@RequiredArgsConstructor
public class ApplicationUserSettingsController {

    private final ApplicationUserSettingsService applicationUserSettingsService;

    @GetMapping
    public ResponseEntity<ApiResponse> showSettings() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User information retrieved successfully")
                        .data(Map.of("applicationUserDTO", applicationUserSettingsService.getApplicationUser()))
                        .build()
        );
    }

    @PatchMapping("/personal-info")
    public ResponseEntity<ApiResponse> updateApplicationUserInfo(
            @Valid @RequestBody ApplicationUserDTO applicationUserInfo) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User information updated")
                        .data(Map.of("updatedApplicationUserDTO",
                                applicationUserSettingsService.updatePersonalInfo(applicationUserInfo)))
                        .build()
        );
    }

    @PatchMapping("/photo")
    public ResponseEntity<ApiResponse> updateApplicationUserPhoto(@RequestBody MultipartFile file) throws IOException {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User photo updated")
                        .data(Map.of("updatedApplicationUserDTO", applicationUserSettingsService.updatePhoto(file)))
                        .build()
        );
    }

    @DeleteMapping("/photo-delete")
    public ResponseEntity<ApiResponse> deleteApplicationUserPhoto() throws IOException {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Profile photo updated")
                        .data(Map.of("updatedApplicationUserDTO", applicationUserSettingsService.deletePhoto()))
                        .build()
        );
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse> updateApplicationUserPassword(
            @Valid @RequestBody ApplicationUserDTO applicationUserInfo) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User password updated")
                        .data(Map.of("updatedApplicationUserDTO",
                                applicationUserSettingsService.updatePassword(applicationUserInfo)))
                        .build()
        );
    }

    @DeleteMapping("/account-delete")
    public ResponseEntity<ApiResponse> deleteApplicationUser() {
        applicationUserSettingsService.deleteApplicationUser();

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Account has been deleted!")
                        .build()
        );
    }
}
