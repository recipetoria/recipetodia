package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.dto.TagDTO;
import com.jit.rec.recipetoria.entity.Tag;
import com.jit.rec.recipetoria.repository.TagRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public void createNewTag(TagDTO tagDTO) {
        Tag newTag = new Tag();
        newTag.setName(tagDTO.getName());
        //newTag.setApplicationUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        tagRepository.save(newTag);
    }

    public Tag getTagById(Long id){
        return tagRepository.findById(id).orElseThrow(()-> new IllegalStateException("TAG_NOT_FOUND"));
    }

    public List<Tag> getAllTags(){
        List<Tag> allTags = new ArrayList<>();
        allTags = tagRepository.findAll();
        return allTags;
    }

    public void deleteById(Long id){
        tagRepository.deleteById(id);
    }

}
