package com.jit.rec.recipetoria.applicationUser.settings;

import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserDTO;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserDTOMapper;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserRepository;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import com.jit.rec.recipetoria.filestorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationUserSettingsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final ApplicationUserDTOMapper applicationUserDTOMapper;
    private final MessageSource messageSource;

    public ApplicationUserDTO getApplicationUser() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return applicationUserDTOMapper.apply(applicationUser);
    }

    public void updatePersonalInfo(ApplicationUserDTO applicationUserInfo) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (applicationUserInfo.name() != null) {
            applicationUser.setName(applicationUserInfo.name());
        }

        applicationUserRepository.save(applicationUser);
    }

    public byte[] getProfilePhoto() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        if (StringUtils.isBlank(applicationUser.getProfilePhoto())) {
//            throw new ResourceNotFoundException(messageSource.getMessage(
//                    "exception.applicationUserSettings.getProfilePhoto.notFound", null, Locale.getDefault()));
//        }

        return fileStorageService.getPhoto(applicationUser.getProfilePhoto());
    }

    public void updateProfilePhoto(MultipartFile file) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        fileStorageService.validatePhoto(file);

        try {
            deleteProfilePhoto();
            String profilePhotoPath = fileStorageService.putProfilePhoto(applicationUser.getId(), file.getBytes(), file.getOriginalFilename());
            applicationUser.setProfilePhoto(profilePhotoPath);
        } catch (IOException e) {
            throw new RuntimeException(messageSource.getMessage(
                    "exception.applicationUserSettings.updateProfilePhoto.notUploaded",
                    null,
                    Locale.getDefault()), e
            );
        }

        applicationUserRepository.save(applicationUser);
    }

    public void deleteProfilePhoto() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        fileStorageService.deletePhoto(applicationUser.getProfilePhoto());

        applicationUser.setProfilePhoto(null);

        applicationUserRepository.save(applicationUser);
    }

    public boolean checkPasswordMatches(ApplicationUserDTO applicationUserInfo) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return passwordEncoder.matches(applicationUserInfo.password(), applicationUser.getPassword());
    }

    public void updatePassword(ApplicationUserDTO applicationUserInfo) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String encodedPassword = passwordEncoder.encode(applicationUserInfo.password());
        applicationUser.setPassword(encodedPassword);

        applicationUserRepository.save(applicationUser);
    }

    public void deleteApplicationUser() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        deleteProfilePhoto();

        applicationUserRepository.delete(applicationUser);
    }
}
