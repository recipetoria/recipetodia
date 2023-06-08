package com.jit.rec.recipetoria.tag;

import com.jit.rec.recipetoria.recipe.Recipe;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagDTOMapper tagDTOMapper;
    private final MessageSource messageSource;

    public List<TagDTO> getAllTags() {
        List<TagDTO> allTagDTOs = new ArrayList<>();
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Tag> allTags = tagRepository.findTagsByApplicationUser(applicationUser);
        for (Tag tag : allTags) {
            allTagDTOs.add(tagDTOMapper.apply(tag));
        }
        return allTagDTOs;
    }

    public TagDTO createTag(TagDTO newTagInfo) {
        Tag newTag = new Tag();
        Optional.ofNullable(newTagInfo.name())
                        .ifPresent(newTag::setName);
        newTag.setApplicationUser(
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

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

    public TagDTO updateTagById(Long tagId, TagDTO updatedTag) {
        Tag tagToBeUpdated = tagRepository.findById(tagId).
                orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.tag.notFound", null, Locale.getDefault())));
        Optional.ofNullable(updatedTag.name())
                .ifPresent(tagToBeUpdated::setName);
        tagToBeUpdated.setIcon(updatedTag.icon());

        return tagDTOMapper.apply(tagRepository.save(tagToBeUpdated));
    }

    public void deleteTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
                    "exception.tag.notFound", null, Locale.getDefault())));

        for (Recipe recipe : tag.getRecipes()){
            recipe.getTags().remove(tag);
        }

        tagRepository.deleteById(tagId);
    }
}
