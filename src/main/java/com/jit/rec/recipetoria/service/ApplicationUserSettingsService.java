package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserRepository;
import com.jit.rec.recipetoria.security.authentication.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApplicationUserSettingsService {

    private static final String DIRECTORY_FOR_USER_PHOTOS = "src/main/resources/static/images/user-photos/";

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    public ApplicationUser getApplicationUser() {
        return (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean checkIfExists(String email) {
        return applicationUserRepository.findByEmail(email).isPresent();
    }

    public void updatePersonalInfo(RegistrationRequest registrationRequest) {
        ApplicationUser applicationUser = getApplicationUser();

        boolean emailExists = checkIfExists(registrationRequest.getEmail());
        if (!emailExists || registrationRequest.getEmail().equals(applicationUser.getEmail())) {
            applicationUser.setEmail(registrationRequest.getEmail());
            applicationUser.setName(registrationRequest.getName());
            applicationUserRepository.save(applicationUser);
        }
    }

    public void updatePassword(String password) {
        ApplicationUser applicationUser = getApplicationUser();

        String encodedPassword = passwordEncoder.encode(password);
        applicationUser.setPassword(encodedPassword);

        applicationUserRepository.save(applicationUser);
    }

    public void updatePhoto(MultipartFile file) throws IOException {
        ApplicationUser applicationUser = getApplicationUser();

        if (!file.isEmpty()) {
            String fileName = applicationUser.getId() + "-user-photo" + Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));

            deletePhoto();

            Path newFileNameAndPath = Paths.get(DIRECTORY_FOR_USER_PHOTOS, fileName);
            Files.write(newFileNameAndPath, file.getBytes());

            applicationUser.setPhoto(fileName);
            applicationUserRepository.save(applicationUser);
        }
    }

    public void deletePhoto() throws IOException {
        ApplicationUser applicationUser = getApplicationUser();

        if (applicationUser.getPhoto() != null) {
            Path oldFileNameAndPath = Paths.get(DIRECTORY_FOR_USER_PHOTOS, applicationUser.getPhoto());
            Files.delete(oldFileNameAndPath);
            applicationUser.setPhoto(null);
        }
    }

    public void deleteApplicationUser() {
        ApplicationUser applicationUser = getApplicationUser();
        applicationUserRepository.delete(applicationUser);
    }
}
