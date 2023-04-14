package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.entity.NewTagRequest;
import com.jit.rec.recipetoria.entity.Tag;
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
    public void createNewTag(@RequestBody NewTagRequest newTagRequest){
        tagService.createNewTag(newTagRequest);
    }

    @GetMapping
    public List<Tag> getAllTags(){
        return tagService.getAllTags();
    }

    @GetMapping("/{tagId}")
    public Tag getTagById(@PathVariable ("tagId") Long id){
        return tagService.getTagById(id);
    }

    @DeleteMapping("/{tagId}")
    public void deleteTagById(@PathVariable ("tagId") Long id){
        tagService.deleteById(id);
    }

}
