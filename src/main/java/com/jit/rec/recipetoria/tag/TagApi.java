package com.jit.rec.recipetoria.tag;

import com.jit.rec.recipetoria.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

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
    @Operation(summary = "Get a list of all tags of current ApplicationUser",
            description = """
                    send: \n
                    response is array of objects "TagDTO": \n
                        "createdTagDTO": \n
                                   "id": 4, \n
                                   "name": "third tag", \n
                                   "icon": null, -- url of a pic \n
                                   "applicationUserId": 1, -- owner of a tag \n
                                   "recipeIds": [] -- a list of recipe ids are tagged by this tag \n
                    """)
    ResponseEntity<Response> getAllTags();

    @Operation(summary = "Get a tag by id",
            description = """
                    send: \n
                         "id":"Long" \n
                    response is an object "TagDTO": \n
                        "createdTagDTO": \n
                                   "id": 4,  \n
                                   "name": "third tag", \n
                                   "icon": null, -- url of a pic \n
                                   "applicationUserId": 1, -- owner of a tag \n
                                   "recipeIds": [] -- a list of recipe ids are tagged by this tag \n
                    """)
    ResponseEntity<Response> getTagById(Long tagId);

    @Operation(summary = "Create a new Tag",
            description = """
                    send:{ \n
                          "name": "string", \n
                          "icon": "string" \n
                          } \n
                    response is a created object"TagDTO": \n
                        createdTagDTO": { \n
                             "id": 4, \n
                             "name": "tag3", \n
                             "icon": null,  -- url of a pic \n
                             "applicationUserId": 1, -- owner of a tag \n
                             "recipeIds": []  -- a list of recipe ids are tagged by this tag \n
                              } \n
                                        """)
    ResponseEntity<Response> createTag(TagDTO newTagDTO);

    @Operation(summary = "Update a Tag by id",
            description = """
                    send: \n
                          "id": "Long", --REQUIRED \n
                          "TagDTO" --new info for a tag name is REQUIRED \n
                                "name": "updated name", \n
                                "icon": updated url,  -- url of a pic \n
                          \n
                    response: updated object "TagDTO" with full info \n
                        Updated TagDTO: \n
                             "id": 4,  --same id \n
                             "name": "updated name", \n
                             "icon": updated url,  -- url of a pic \n
                             "applicationUserId": 1, -- owner of a tag \n
                             "recipeIds": []  -- a list of recipe ids are tagged by this tag \n
                                        """)
    ResponseEntity<Response> updateTagById(Long tagId, TagDTO updatedTag);

    @Operation(summary = "Delete a tag by id",
            description = """
                    send:{ \n
                        "id":"Long" \n
                    response: status 200 (OK) \n
                    """)
    ResponseEntity<Response> deleteTagById(Long tagId);

}
