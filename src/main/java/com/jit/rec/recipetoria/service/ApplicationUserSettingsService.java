package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.dto.ApplicationUserDTO;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationUserSettingsService {

    private static final String DIRECTORY_FOR_USER_PHOTOS = "src/main/resources/static/images/user-photos/";

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    public ApplicationUserDTO getApplicationUser() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ApplicationUserDTO.convertToDTO(applicationUser);
    }

    public boolean checkIfExists(String email) {
        return applicationUserRepository.findByEmail(email).isPresent();
    }

    public ApplicationUserDTO updatePersonalInfo(ApplicationUserDTO applicationUserInfo) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (applicationUserInfo.email() != null) {
            boolean emailExists = checkIfExists(applicationUserInfo.email());
            if (!emailExists || applicationUserInfo.email().equals(applicationUser.getEmail())) {
                applicationUser.setEmail(applicationUserInfo.email());
            }
        }

        if (applicationUserInfo.name() != null) {
            applicationUser.setName(applicationUserInfo.name());
        }

        ApplicationUser updatedApplicationUser = applicationUserRepository.save(applicationUser);

        return ApplicationUserDTO.convertToDTO(updatedApplicationUser);
    }

    public ApplicationUserDTO updatePhoto(MultipartFile file) throws IOException {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        validatePhoto(file);

        String fileName = applicationUser.getId() + "-profile-photo"
                + Objects.requireNonNull(file.getOriginalFilename()).toLowerCase()
                .substring(file.getOriginalFilename().toLowerCase().lastIndexOf("."));

        deletePhoto();

        Path newFileNameAndPath = Paths.get(DIRECTORY_FOR_USER_PHOTOS, fileName);
        Files.write(newFileNameAndPath, file.getBytes());

        applicationUser.setPhoto(fileName);

        ApplicationUser updatedApplicationUser = applicationUserRepository.save(applicationUser);

        return ApplicationUserDTO.convertToDTO(updatedApplicationUser);

    }

    private void validatePhoto(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        long maxSize = 5 * 1024 * 1024; // 5 MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File size exceeds the allowed limit of 5 MB.");
        }

        Set<String> allowedExtensions = new HashSet<>(Arrays.asList(".jpg", ".jpeg", ".png", ".gif"));
        boolean isAllowedExtension = false;
        for (String extension : allowedExtensions) {
            if (Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(extension)) {
                isAllowedExtension = true;
                break;
            }
        }
        if (!isAllowedExtension) {
            throw new IllegalArgumentException("File extension is not allowed. Allowed extensions are: "
                    + allowedExtensions);
        }
    }

    public ApplicationUserDTO deletePhoto() throws IOException {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (applicationUser.getPhoto() != null) {
            Path oldFileNameAndPath = Paths.get(DIRECTORY_FOR_USER_PHOTOS, applicationUser.getPhoto());
            Files.delete(oldFileNameAndPath);
            applicationUser.setPhoto(null);
        }

        ApplicationUser updatedApplicationUser = applicationUserRepository.save(applicationUser);

        return ApplicationUserDTO.convertToDTO(updatedApplicationUser);
    }

    public ApplicationUserDTO updatePassword(ApplicationUserDTO applicationUserInfo) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String encodedPassword = passwordEncoder.encode(applicationUserInfo.password());
        applicationUser.setPassword(encodedPassword);

        ApplicationUser updatedApplicationUser = applicationUserRepository.save(applicationUser);

        return ApplicationUserDTO.convertToDTO(updatedApplicationUser);
    }

    public void deleteApplicationUser() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        applicationUserRepository.delete(applicationUser);
    }
}
