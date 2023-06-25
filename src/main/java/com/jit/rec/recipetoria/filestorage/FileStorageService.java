package com.jit.rec.recipetoria.filestorage;

import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorageProperties fileStorageProperties;
    private final MessageSource messageSource;

    public byte[] getPhoto(String fileDirectoryAndName) {
        try {
            return Files.readAllBytes(Path.of(fileDirectoryAndName));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read bytes");
        }
    }

    public String putProfilePhoto(Long applicationUserId, byte[] fileBytes, String originalFileName) {

        String profilePhotoDirectory = fileStorageProperties.getProfilePhotoDirectory().formatted(applicationUserId);
        String profilePhotoName = fileStorageProperties.getProfilePhotoName().formatted(applicationUserId, getFileExtension(originalFileName));

        putPhoto(profilePhotoDirectory, profilePhotoName, fileBytes);

        return profilePhotoDirectory + profilePhotoName;
    }

    public String putTagMainPhoto(Long applicationUserId, Long tagId, byte[] fileBytes, String originalFileName) {
        String tagMainPhotoDirectory = fileStorageProperties.getTagMainPhotoDirectory().formatted(applicationUserId);
        String tagMainPhotoName = fileStorageProperties.getTagMainPhotoName().formatted(tagId, getFileExtension(originalFileName));

        putPhoto(tagMainPhotoDirectory, tagMainPhotoName, fileBytes);

        return tagMainPhotoDirectory + tagMainPhotoName;
    }

    public String putRecipeMainPhoto(Long applicationUserId, Long recipeId, byte[] fileBytes, String originalFileName) {
        String recipeMainPhotoDirectory = fileStorageProperties.getRecipeMainPhotoDirectory().formatted(applicationUserId);
        String recipeMainPhotoName = fileStorageProperties.getRecipeMainPhotoName().formatted(recipeId, getFileExtension(originalFileName));

        putPhoto(recipeMainPhotoDirectory, recipeMainPhotoName, fileBytes);

        return recipeMainPhotoDirectory + recipeMainPhotoName;
    }

    public String putRecipeInstructionPhoto(Long applicationUserId,
                                            Long recipeId,
                                            byte[] fileBytes,
                                            String originalFileName,
                                            int instructionPhotoUniqueNumber) {
        String recipeInstructionPhotoDirectory = fileStorageProperties.getRecipeInstructionPhotoDirectory()
                .formatted(applicationUserId);
        String recipeInstructionPhotoName = fileStorageProperties.getRecipeInstructionPhotoName()
                .formatted(recipeId, instructionPhotoUniqueNumber ,getFileExtension(originalFileName));

        putPhoto(recipeInstructionPhotoDirectory, recipeInstructionPhotoName, fileBytes);

        return recipeInstructionPhotoDirectory + recipeInstructionPhotoName;
    }

    public void deletePhoto(String photo) {
        if (photo != null) {
            try {
                Path oldFileNameAndPath = Paths.get(photo);
                Files.delete(oldFileNameAndPath);
            } catch (IOException e) {
                throw new RuntimeException(messageSource.getMessage(
                        "exception.fileStorage.deletePhoto.notDeleted", null, Locale.getDefault()));
            }
        }
    }

    public void validatePhoto(MultipartFile file) {
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

    private void putPhoto(String photoDirectory, String photoName, byte[] fileBytes) {
        try {
            Files.createDirectories(Path.of(photoDirectory));
            Files.write(Path.of(photoDirectory + photoName), fileBytes);
        } catch (IOException e) {
            throw new ResourceNotFoundException(messageSource.getMessage(
                    "exception.fileStorage.putPhoto.notFound", null, Locale.getDefault()));
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
