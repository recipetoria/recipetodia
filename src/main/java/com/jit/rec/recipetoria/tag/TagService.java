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

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagDTOMapper tagDTOMapper;
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

    public TagDTO createTag(TagDTO newTagDTO) { //TODO: add support for icons
        Tag newTag = new Tag();

        if (newTagDTO.name() != null) {
            newTag.setName(newTagDTO.name());
        }
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

    public void updateTagById(Long tagId, TagDTO updatedTagInfo) {
        Tag tagToBeUpdated = getTagById(tagId);

        if (updatedTagInfo.name() != null) {
            tagToBeUpdated.setName(updatedTagInfo.name());
        }
        tagToBeUpdated.setIcon(updatedTagInfo.icon());

        tagRepository.save(tagToBeUpdated);
    }

    public void deleteTagById(Long tagId) {
        Tag tag = getTagById(tagId);

        for (Recipe recipe : tag.getRecipes()){
            recipe.getTags().remove(tag);
        }

        tagRepository.deleteById(tagId);
    }
}
