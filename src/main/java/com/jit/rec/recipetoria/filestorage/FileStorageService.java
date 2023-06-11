package com.jit.rec.recipetoria.filestorage;

import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorageProperties fileStorageProperties;

    public byte[] getProfilePhoto(String profilePhotoDirectoryAndName) {
        try {
            return Files.readAllBytes(Path.of(profilePhotoDirectoryAndName));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read bytes");
        }
    }

    public String putProfilePhoto(Long applicationUserId, byte[] fileBytes, String originalFileName) {

        String profilePhotoDirectory = fileStorageProperties.getProfileImageDirectory().formatted(applicationUserId);
        String profilePhotoName = fileStorageProperties.getProfileImageName().formatted(applicationUserId, getFileExtension(originalFileName));

        try {
            Files.createDirectories(Path.of(profilePhotoDirectory));
            Files.write(Path.of(profilePhotoDirectory + profilePhotoName), fileBytes);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Profile image not found");
        }

        return profilePhotoDirectory + profilePhotoName;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
