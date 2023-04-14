package com.jit.rec.recipetoria.service;

import com.jit.rec.recipetoria.entity.NewTagRequest;
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

    public void createNewTag(NewTagRequest newTagRequest) {
        Tag newTag = new Tag();
        newTag.setName(newTagRequest.getName());
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
