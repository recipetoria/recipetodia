package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.TagDTO;
import com.jit.rec.recipetoria.entity.ApiResponse;
import com.jit.rec.recipetoria.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<ApiResponse> createNewTag(@RequestBody TagDTO tagDTO){
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .message("tag \"" + tagDTO.getName() + "\" was created")
                        .data(Map.of("tagDTO", tagService.createNewTag(tagDTO)))
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTags(){
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("all tags of user retrieved")
                        .data(Map.of("tagDTO", tagService.getAllTagsOfUser()))
                        .build()
        );
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<ApiResponse> getTagById(@PathVariable ("tagId") Long id){
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("tag with ID " + id + " retrieved")
                        .data(Map.of("tagDTO", tagService.getTagById(id)))
                        .build()
        );
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<ApiResponse> deleteTagById(@PathVariable ("tagId") Long id){
        tagService.deleteById(id);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("tag with ID " + id + " was deleted")
                        .build()
        );
    }
}
