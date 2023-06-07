package com.jit.rec.recipetoria.applicationUser.settings;

import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserDTO;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationUserSettingsService {

    private static final String PROFILE_PHOTO_DIRECTORY_DEFAULT = "static/images/"; //TODO: move to .properties
    private static final String PROFILE_PHOTO_NAME_DEFAULT = "default-profile-photo.png";
    private static final String RESOURCES_DIRECTORY = "src/main/resources/";
    private static final String PROFILE_PHOTO_DIRECTORY = "static/images/user-%d/profile-photo/";
    private static final String PROFILE_PHOTO_NAME = "%d-profile-photo";

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

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

        if (applicationUserInfo.name() != null) {
            applicationUser.setName(applicationUserInfo.name());
        }

        ApplicationUser updatedApplicationUser = applicationUserRepository.save(applicationUser);

        return ApplicationUserDTO.convertToDTO(updatedApplicationUser);
    }

    public ApplicationUserDTO updateProfilePhoto(MultipartFile file) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        validatePhoto(file);

        return updateProfilePhoto(applicationUser, file, null, file.getOriginalFilename());
    }

    public ApplicationUserDTO updateProfilePhoto() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        deletePhoto();
        return updateProfilePhoto(applicationUser);
    }

    public ApplicationUserDTO updateProfilePhoto(ApplicationUser applicationUser) {

        return updateProfilePhoto(applicationUser, null, PROFILE_PHOTO_DIRECTORY_DEFAULT, PROFILE_PHOTO_NAME_DEFAULT);
    }

    private ApplicationUserDTO updateProfilePhoto(ApplicationUser applicationUser,
                                                  MultipartFile file,
                                                  String sourceDirectory, String sourceName) {
        String fileName = String.format(PROFILE_PHOTO_NAME, applicationUser.getId())
                + getFileExtension(Objects.requireNonNull(sourceName));

        if (file != null) {
            try {
                deletePhoto();
                Path newFileNameAndPath = Paths.get(
                        RESOURCES_DIRECTORY + String.format(PROFILE_PHOTO_DIRECTORY, applicationUser.getId()), fileName
                );
                Files.write(newFileNameAndPath, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e); // TODO: handle exception
            }
        } else {
            Path targetPath =
                    Paths.get(RESOURCES_DIRECTORY + String.format(PROFILE_PHOTO_DIRECTORY, applicationUser.getId()));
            try {
                Files.createDirectories(targetPath);
            } catch (IOException e) {
                throw new RuntimeException(e); // TODO: handle exception
            }

            Path profilePhotoNew = targetPath.resolve(
                    String.format(PROFILE_PHOTO_NAME, applicationUser.getId()) + getFileExtension(sourceName)
            );

            try (InputStream inputStream = new ClassPathResource(sourceDirectory + sourceName).getInputStream();
                 OutputStream outputStream = Files.newOutputStream(profilePhotoNew)) {
                inputStream.transferTo(outputStream);
            } catch (IOException e) {
                try {
                    throw new IOException(messageSource.getMessage(
                            "exception.applicationUserSettings.updateProfilePhoto.notCreated", null, Locale.getDefault()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex); // TODO: handle exception
                }
            }
        }

        applicationUser.setPhoto(String.format(PROFILE_PHOTO_DIRECTORY, applicationUser.getId()) + fileName);
        return ApplicationUserDTO.convertToDTO(applicationUserRepository.save(applicationUser));
    }

    private void validatePhoto(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "exception.applicationUserSettings.validatePhoto.emptyFile", null, Locale.getDefault()));
        }

        long maxSize = 5 * 1024 * 1024; // 5 MB
        if (file.getSize() > maxSize) {
            throw new MaxUploadSizeExceededException(maxSize);
        }

        Set<String> supportedExtensions = new HashSet<>(
                List.of(messageSource.getMessage("profilePhoto.supportedExtensions", null, Locale.getDefault()).split(","))
        );
        boolean isAllowedExtension = false;
        for (String extension : supportedExtensions) {
            if (Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(extension)) {
                isAllowedExtension = true;
                break;
            }
        }
        if (!isAllowedExtension) {
            String message = messageSource.getMessage(
                    "exception.applicationUserSettings.validatePhoto.invalidExtension", null, Locale.getDefault());
            throw new IllegalArgumentException(message + " " + supportedExtensions);
        }
    }

    private void deletePhoto() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (applicationUser.getPhoto() != null) {
            try {
                Path oldFileNameAndPath = Paths.get(RESOURCES_DIRECTORY + applicationUser.getPhoto());
                Files.delete(oldFileNameAndPath);
            } catch (IOException e) {
                throw new RuntimeException(e); // TODO: handle exception
            }
            applicationUser.setPhoto(null);
        }

        applicationUserRepository.save(applicationUser);
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
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

        deletePhoto();

        applicationUserRepository.delete(applicationUser);
    }
}
