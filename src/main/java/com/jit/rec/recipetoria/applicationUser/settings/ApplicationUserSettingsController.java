package com.jit.rec.recipetoria.applicationUser.settings;

import com.jit.rec.recipetoria.applicationUser.ApplicationUserDTO;
import com.jit.rec.recipetoria.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/settings")
@RequiredArgsConstructor
public class ApplicationUserSettingsController implements ApplicationUserSettingsApi {

    private final ApplicationUserSettingsService applicationUserSettingsService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<Response> showSettings() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.userSettings.showSettings", null, Locale.getDefault()))
                        .data(Map.of("applicationUserDTO", applicationUserSettingsService.getApplicationUser()))
                        .build());
    }

    @PutMapping("/personal-info")
    public ResponseEntity<Response> updateApplicationUserInfo(
            @Valid @RequestBody ApplicationUserDTO applicationUserInfo) {
        applicationUserSettingsService.updatePersonalInfo(applicationUserInfo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.userSettings.updateApplicationUserInfo", null, Locale.getDefault()))
                        .build());
    }

    @GetMapping("/photo")
    public ResponseEntity<Response> getApplicationUserPhoto() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.userSettings.getApplicationUserPhoto", null, Locale.getDefault()))
                        .data(Map.of("profilePhoto", applicationUserSettingsService.getProfilePhoto()))
                        .build());
    }

    @PutMapping("/photo")
    public ResponseEntity<Response> updateApplicationUserPhoto(@RequestBody MultipartFile file) {
        applicationUserSettingsService.updateProfilePhoto(file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.userSettings.updateApplicationUserPhoto", null, Locale.getDefault()))
                        .build());
    }

    @DeleteMapping("/photo")
    public ResponseEntity<Response> deleteApplicationUserPhoto() {
        applicationUserSettingsService.deleteProfilePhoto();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message(messageSource.getMessage(
                                "response.userSettings.deleteApplicationUserPhoto", null, Locale.getDefault()))
                        .build());
    }

    @PostMapping("/password")
    public ResponseEntity<Response> checkApplicationUserPassword(
            @Valid @RequestBody ApplicationUserDTO applicationUserInfo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.userSettings.checkApplicationUserPassword", null, Locale.getDefault()))
                        .data(Map.of("matches", applicationUserSettingsService.checkPasswordMatches(applicationUserInfo)))
                        .build());
    }

    @PutMapping("/password")
    public ResponseEntity<Response> updateApplicationUserPassword(
            @Valid @RequestBody ApplicationUserDTO applicationUserInfo) {
        applicationUserSettingsService.updatePassword(applicationUserInfo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.userSettings.updateApplicationUserPassword", null, Locale.getDefault()))
                        .build());
    }

    @DeleteMapping("/account")
    public ResponseEntity<Response> deleteApplicationUser() {
        applicationUserSettingsService.deleteApplicationUser();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message(messageSource.getMessage(
                                "response.userSettings.deleteApplicationUser", null, Locale.getDefault()))
                        .build());
    }
}
