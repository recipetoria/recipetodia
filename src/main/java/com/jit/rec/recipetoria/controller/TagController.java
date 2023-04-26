package com.jit.rec.recipetoria.controller;

import com.jit.rec.recipetoria.dto.TagDTO;
import com.jit.rec.recipetoria.entity.ApiResponse;
import com.jit.rec.recipetoria.service.TagService;
import com.jit.rec.recipetoria.swagger.TagControllerInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/client/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TagController implements TagControllerInterface {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTags() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Tags retrieved")
                        .data(Map.of("allTagsDTOs", tagService.getAllTags()))
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTag(@RequestBody @Valid TagDTO newTagDTO) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .message("Tag \"" + newTagDTO.name() + "\" created")
                        .data(Map.of("createdTagDTO", tagService.createTag(newTagDTO)))
                        .build()
        );
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<ApiResponse> getTagById(@PathVariable("tagId") Long tagId) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Tag with ID " + tagId + " retrieved")
                        .data(Map.of("tagDTO", tagService.getTagDTOById(tagId)))
                        .build()
        );
    }

    @PatchMapping("/{tagId}")
    public ResponseEntity<ApiResponse> updateTagById(@PathVariable("tagId") Long tagId,
                                                     TagDTO updatedTag){
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Tag with id " + tagId + " updated")
                        .data(Map.of("updated tagDTO", tagService.updateTagById(tagId, updatedTag)))
                        .build()
        );
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<ApiResponse> deleteTagById(@PathVariable("tagId") Long tagId) {
        tagService.deleteTagById(tagId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Tag with ID " + tagId + " has been deleted")
                        .build()
        );
    }
}
