package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.dto.TagDTO;
import com.jit.rec.recipetoria.entity.Tag;
import com.jit.rec.recipetoria.repository.TagRepository;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Long createNewTag(TagDTO tagDTO) {
        Tag newTag = new Tag();
        newTag.setName(tagDTO.getName());
        newTag.setApplicationUser((ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return tagRepository.save(newTag).getId();
    }

    public TagDTO getTagById(Long tagId){
        return TagDTO.convertToTagDto(tagRepository.findById(tagId).orElseThrow(()-> new IllegalStateException("TAG_NOT_FOUND")));
    }

    public List<TagDTO> getAllTagsOfUser(Long userId){
        List<TagDTO> tagDTOList = new ArrayList<>();
        List<Tag>allTags = tagRepository.findTagsByApplicationUser_Id(userId);
        for(Tag tag : allTags){
            tagDTOList.add(TagDTO.convertToTagDto(tag));
        }
        return tagDTOList;
    }

    public void deleteById(Long id){
        tagRepository.deleteById(id);
    }

}
