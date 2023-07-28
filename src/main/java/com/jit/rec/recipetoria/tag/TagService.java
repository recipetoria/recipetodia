package com.jit.rec.recipetoria.tag;

import com.jit.rec.recipetoria.filestorage.FileStorageService;
import com.jit.rec.recipetoria.recipe.Recipe;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagDTOMapper tagDTOMapper;
    private final FileStorageService fileStorageService;
    private final MessageSource messageSource;

    public List<TagDTO> getAllTags() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Tag> allTags = tagRepository.findAllByApplicationUser(applicationUser);

        List<TagDTO> allTagDTOs = new ArrayList<>();
        for (Tag oneTag : allTags) {
            allTagDTOs.add(tagDTOMapper.apply(oneTag));
        }

        return allTagDTOs;
    }

    public TagDTO createTag(TagDTO newTagDTO, ApplicationUser applicationUser) {
        Tag newTag = new Tag();

        if (newTagDTO.name() != null) {
            newTag.setName(newTagDTO.name());
        }
        if (applicationUser != null) {
            newTag.setApplicationUser(applicationUser);
        } else {
            newTag.setApplicationUser(
                    (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }

        return tagDTOMapper.apply(tagRepository.save(newTag));
    }

    public TagDTO getTagDTOById(Long tagId) {
        return tagDTOMapper.apply(getTagById(tagId));
    }

    public Tag getTagById(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.tag.notFound", null, Locale.getDefault())));
    }

    public void updateTagById(Long tagId, TagDTO updatedTagInfo, MultipartFile file) {
        Tag tagToBeUpdated = getTagById(tagId);

        if (updatedTagInfo != null) {
            updateTagName(tagToBeUpdated, updatedTagInfo);
        }
        if (file != null) {
            updateTagMainPhoto(tagToBeUpdated, file);
        }

        tagRepository.save(tagToBeUpdated);
    }

    public void deleteTagById(Long tagId) {
        Tag tag = getTagById(tagId);

        for (Recipe recipe : tag.getRecipes()) {
            recipe.getTags().remove(tag);
        }

        tagRepository.deleteById(tagId);
    }

    public byte[] getTagMainPhoto(Long tagId) {
        Tag tag = getTagById(tagId);

        if (StringUtils.isBlank(tag.getMainPhoto())) {
            throw new ResourceNotFoundException(messageSource.getMessage(
                    "exception.tag.getMainPhoto.notFound", null, Locale.getDefault()));
        }

        return fileStorageService.getPhoto(tag.getMainPhoto());
    }

    private void updateTagName(Tag tagToBeUpdated, TagDTO updatedTagInfo) {
        if (updatedTagInfo.name() != null) {
            tagToBeUpdated.setName(updatedTagInfo.name());
        }
    }

    private void updateTagMainPhoto(Tag tagToBeUpdated, MultipartFile file) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        fileStorageService.validatePhoto(file);

        try {
            deleteTagMainPhoto(tagToBeUpdated);
            String tagMainPhotoPath = fileStorageService.putTagMainPhoto(
                    applicationUser.getId(), tagToBeUpdated.getId(), file.getBytes(), file.getOriginalFilename());
            tagToBeUpdated.setMainPhoto(tagMainPhotoPath);
        } catch (IOException e) {
            throw new RuntimeException(messageSource.getMessage(
                    "exception.tag.updateTagById.notUploaded", null, Locale.getDefault()));
        }
    }

    private void deleteTagMainPhoto(Tag tagToBeUpdated) {
        fileStorageService.deletePhoto(tagToBeUpdated.getMainPhoto());

        tagToBeUpdated.setMainPhoto(null);

        tagRepository.save(tagToBeUpdated);
    }
}
