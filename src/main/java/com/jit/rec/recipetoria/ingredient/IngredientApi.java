package com.jit.rec.recipetoria.ingredient;

import com.jit.rec.recipetoria.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ingredients")
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
public interface IngredientApi {

    @Operation(
            summary = "Get ingredient by ID",
            description = "Retrieves the ingredient"
    )
    @ApiResponse(
            responseCode = "200", description = "Ingredient retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "ingredientId",
                    required = true,
                    description = "Ingredient ID"
            )
    })
    @GetMapping("/{ingredientId}")
    ResponseEntity<Response> getIngredientById(@PathVariable("ingredientId") Long ingredientId);

    @Operation(
            summary = "Update ingredient",
            description = "Updates ingredient"
    )
    @ApiResponse(
            responseCode = "200", description = "Ingredient updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "ingredientId",
                    required = true,
                    description = "Ingredient ID"
            ),
            @Parameter(
                    name = "updatedIngredientInfo",
                    required = true,
                    description = """
                            New ingredient information \n
                            { \n
                                id: ignored \n
                                name: required \n
                                amount: not required \n
                                measurementUnit: not required \n
                                applicationUserId: ignored
                            } \n
                            """
            )
    })
    @PutMapping("/{ingredientId}")
    ResponseEntity<Response> updateIngredientById(@PathVariable("ingredientId") Long ingredientId,
                                                  IngredientDTO updatedIngredientInfo);

    @Operation(
            summary = "Delete ingredient",
            description = "Deletes ingredient"
    )
    @ApiResponse(
            responseCode = "204", description = "Ingredient deleted successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "ingredientId",
                    required = true,
                    description = "Ingredient ID"
            )
    })
    @DeleteMapping("/{ingredientId}")
    ResponseEntity<Response> deleteIngredientById(@PathVariable("ingredientId") Long ingredientId);
}
