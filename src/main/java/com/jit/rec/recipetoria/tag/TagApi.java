package com.jit.rec.recipetoria.tag;

import com.jit.rec.recipetoria.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tags")
@ApiResponses({
        @ApiResponse(
                responseCode = "400", description = "Invalid data sent to the server",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Response.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "User not authorized",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "User has no access to the resource",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Resource id was not found",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public interface TagApi {

    @Operation(
            summary = "Get all tags for current ApplicationUser",
            description = "Retrieves a list of all tags for current ApplicationUser"
    )
    @ApiResponse(
            responseCode = "200", description = "List of tags for current ApplicationUser retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @GetMapping
    ResponseEntity<Response> getAllTags();

    @Operation(
            summary = "Create new Tag",
            description = "Creates a new Tag for current ApplicationUser"
    )
    @ApiResponse(
            responseCode = "201", description = "Tag created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "newTagInfo",
                    required = true,
                    description = """
                            New tag information \n
                            { \n
                                id: ignored \n
                                name: required \n
                                icon: not required \n
                                applicationUserId: ignored \n
                                recipeIds: ignored \n
                            } \n
                            """
            )
    })
    @PostMapping
    ResponseEntity<Response> createTag(TagDTO newTagDTO);

    @Operation(
            summary = "Get tag by ID",
            description = "Retrieves the tag"
    )
    @ApiResponse(
            responseCode = "200", description = "Tag retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "tagId",
                    required = true,
                    description = "Tag ID"
            )
    })
    @GetMapping("/{tagId}")
    ResponseEntity<Response> getTagById(@PathVariable("tagId") Long tagId);

    @Operation(
            summary = "Update tag",
            description = "Updates tag"
    )
    @ApiResponse(
            responseCode = "200", description = "Tag updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "tagId",
                    required = true,
                    description = "Tag ID"
            ),
            @Parameter(
                    name = "updatedTagInfo",
                    required = true,
                    description = """
                            New tag information \n
                            { \n
                                id: ignored \n
                                name: required \n
                                icon: not required \n
                                applicationUserId: ignored \n
                                recipeIds: ignored \n
                            } \n
                            """
            )
    })
    @PutMapping("/{tagId}")
    ResponseEntity<Response> updateTagById(@PathVariable("tagId") Long tagId,
                                           @RequestBody @Valid TagDTO updatedTag);

    @Operation(
            summary = "Delete tag",
            description = "Deletes tag"
    )
    @ApiResponse(
            responseCode = "204", description = "Tag deleted successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "tagId",
                    required = true,
                    description = "Tag ID"
            )
    })
    @DeleteMapping("/{tagId}")
    ResponseEntity<Response> deleteTagById(@PathVariable("tagId") Long tagId);
}
