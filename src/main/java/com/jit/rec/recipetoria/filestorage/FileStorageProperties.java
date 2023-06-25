package com.jit.rec.recipetoria.filestorage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file-storage")
@Getter
@Setter
public class FileStorageProperties {

    private String profilePhotoDirectory;
    private String profilePhotoName;

    private String tagMainPhotoDirectory;
    private String tagMainPhotoName;

    private String recipeMainPhotoDirectory;
    private String recipeMainPhotoName;

    private String recipeInstructionPhotoDirectory;
    private String recipeInstructionPhotoName;
}
