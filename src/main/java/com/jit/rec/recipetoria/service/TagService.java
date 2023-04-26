package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.dto.TagDTO;
import com.jit.rec.recipetoria.entity.Tag;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import com.jit.rec.recipetoria.repository.TagRepository;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<TagDTO> getAllTags() {
        List<TagDTO> allTagDTOs = new ArrayList<>();
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Tag> allTags = tagRepository.findTagsByApplicationUser(applicationUser);
        for (Tag tag : allTags) {
            allTagDTOs.add(TagDTO.convertToDTO(tag));
        }
        return allTagDTOs;
    }

    public TagDTO createTag(TagDTO newTagInfo) {
        Tag newTag = new Tag();
        newTag.setName(newTagInfo.name());
        newTag.setApplicationUser(
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return TagDTO.convertToDTO(tagRepository.save(newTag));
    }

    public TagDTO getTagDTOById(Long tagId) {
        return TagDTO.convertToDTO(getTagById(tagId));
    }

    public Tag getTagById(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with ID: " + tagId + " not found!"));
    }

    public TagDTO updateTagById(Long tagId, TagDTO updatedTag){
        Tag tagToBeUpdated = tagRepository.findById(tagId).
                orElseThrow(() -> new ResourceNotFoundException("Tag with ID: " + tagId + " not found!"));
        tagToBeUpdated.setName(updatedTag.name());
        tagToBeUpdated.setIcon(updatedTag.icon());

        return TagDTO.convertToDTO(tagRepository.save(tagToBeUpdated));
    }

    public void deleteTagById(Long tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new ResourceNotFoundException("Tag with ID: " + tagId + " not found!");
        }
        tagRepository.deleteById(tagId);
    }
}
