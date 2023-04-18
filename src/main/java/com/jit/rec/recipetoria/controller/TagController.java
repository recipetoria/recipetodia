package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.TagDTO;
import com.jit.rec.recipetoria.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Long createNewTag(@RequestBody TagDTO tagDTO){
        return tagService.createNewTag(tagDTO);
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<TagDTO> getAllTags(@PathVariable Long userId){
        return tagService.getAllTagsOfUser(userId);
    }

    @GetMapping("/{tagId}")
    public TagDTO getTagById(@PathVariable ("tagId") Long id){
        return tagService.getTagById(id);
    }

    @DeleteMapping("/{tagId}")
    public void deleteTagById(@PathVariable ("tagId") Long id){
        tagService.deleteById(id);
    }

}
