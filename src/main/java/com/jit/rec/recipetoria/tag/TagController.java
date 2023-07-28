package com.jit.rec.recipetoria.tag;

import com.jit.rec.recipetoria.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/tags")
@RequiredArgsConstructor
public class TagController implements TagApi {

    private final TagService tagService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<Response> getAllTags() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage("response.tag.getAllTags", null, Locale.getDefault()))
                        .data(Map.of("allTagsDTOs", tagService.getAllTags()))
                        .build());
    }

    @PostMapping
    public ResponseEntity<Response> createTag(@RequestBody @Valid TagDTO newTagInfo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .message(messageSource.getMessage("response.tag.createTag", null, Locale.getDefault()))
                        .data(Map.of("createdTagDTO", tagService.createTag(newTagInfo, null)))
                        .build());
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<Response> getTagById(@PathVariable("tagId") Long tagId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage("response.tag.getTagById", null, Locale.getDefault()))
                        .data(Map.of("tagDTO", tagService.getTagDTOById(tagId)))
                        .build());
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<Response> updateTagNameById(@PathVariable("tagId") Long tagId,
                                                      @RequestBody @Valid TagDTO updatedTagInfo) {
        tagService.updateTagById(tagId, updatedTagInfo, null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage("response.tag.updateTagById", null, Locale.getDefault()))
                        .build());
    }

    @PutMapping("/{tagId}/photo")
    public ResponseEntity<Response> updateTagMainPhotoById(@PathVariable("tagId") Long tagId,
                                                           @RequestBody MultipartFile file) {

        tagService.updateTagById(tagId, null, file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage("response.tag.updateTagById", null, Locale.getDefault()))
                        .build());
    }

    @GetMapping("/{tagId}/photo")
    public ResponseEntity<Response> getTagMainPhoto(@PathVariable("tagId") Long tagId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.tag.getTagMainPhoto", null, Locale.getDefault()))
                        .data(Map.of("tagMainPhoto", tagService.getTagMainPhoto(tagId)))
                        .build());
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Response> deleteTagById(@PathVariable("tagId") Long tagId) {
        tagService.deleteTagById(tagId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message(messageSource.getMessage("response.tag.deleteTagById", null, Locale.getDefault()))
                        .build());
    }
}
