package com.jit.rec.recipetoria.recipe;

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

@Tag(name = "Recipes")
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
public interface RecipeApi {

    @Operation(
            summary = "Get all recipes for current ApplicationUser",
            description = """
                    Retrieves all recipes for current ApplicationUser in the following format \n
                    { \n
                        id: Long,
                        name: String, \n
                        mainPhoto: String, \n
                        applicationUserId: Long, \n
                        tagDTOs: {
                            { \n
                                id: Long, \n
                                name: String, \n
                                icon: String, \n
                                applicationUserId: Long \n
                                recipeIds: { \n
                                    Long, \n
                                    ... \n
                                } \n
                            }, \n
                            ... \n
                        }, \n
                        ingredientDTOs: { \n
                            { \n
                                id: Long, \n
                                name: String, \n
                                amount: Double, \n
                                measurementUnit: MeasurementUnit (enum), \n
                                applicationUserId: Long \n
                            }, \n
                            ... \n
                        }, \n
                        instructions: String \n
                        instructionPhotos: {String...} \n
                        links: {String...} \n
                    } \n
                    """
    )
    @ApiResponse(
            responseCode = "200", description = "List of recipes retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @GetMapping
    ResponseEntity<Response> getAllRecipes();

    @Operation(
            summary = "Create new recipe",
            description = """
                    Creates a recipe and returns in the following format
                    { \n
                        id: Long
                        name: String, \n
                        mainPhoto: String, \n
                        applicationUserId: Long, \n
                        tagDTOs: {
                            { \n
                                id: Long, \n
                                name: String, \n
                                icon: String, \n
                                applicationUserId: Long \n
                                recipeIds: {Long...}
                            }, \n
                            ... \n
                        }, \n
                        ingredientDTOs: { \n
                            { \n
                                id: Long, \n
                                name: String, \n
                                amount: Double, \n
                                measurementUnit: MeasurementUnit (enum), \n
                                applicationUserId: Long \n
                            }, \n
                            ... \n
                        }, \n
                        instructions: String \n
                        instructionPhotos: {String...} \n
                        links: {String...} \n
                    } \n
                    """
    )
    @ApiResponse(
            responseCode = "201", description = "Recipe created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "newRecipeInfo",
                    required = true,
                    description = """
                            New recipe information \n
                            { \n
                                id: ignored
                                name: required \n
                                mainPhoto: not required \n
                                tagDTOs: { \n
                                    { \n
                                        id: not required, \n
                                    }, \n
                                    ... \n
                                }, \n
                                ingredientDTOs: { \n
                                    { \n
                                        name: required, \n
                                        amount: not required, \n
                                        measurementUnit: not required, \n
                                    }, \n
                                    ... \n
                                }, \n
                                instructions: not required \n
                                instructionPhotos: not required \n
                                links: not required \n
                            } \n
                            """
            )
    })
    @PostMapping
    ResponseEntity<Response> createRecipe(RecipeDTO newRecipeInfo);

    @Operation(
            summary = "Get recipe by ID",
            description = """
                    Retrieves the recipe in the following format \n
                    { \n
                        id: Long,
                        name: String, \n
                        mainPhoto: String, \n
                        applicationUserId: Long, \n
                        tagDTOs: {
                            { \n
                                id: Long, \n
                                name: String, \n
                                icon: String, \n
                                applicationUserId: Long \n
                                recipeIds: { \n
                                    Long, \n
                                    ... \n
                                } \n
                            }, \n
                            ... \n
                        }, \n
                        ingredientDTOs: { \n
                            { \n
                                id: Long, \n
                                name: String, \n
                                amount: Double, \n
                                measurementUnit: MeasurementUnit (enum), \n
                                applicationUserId: Long \n
                            }, \n
                            ... \n
                        }, \n
                        instructions: String \n
                        instructionPhotos: {String...} \n
                        links: {String...} \n
                    } \n
                    """
    )
    @ApiResponse(
            responseCode = "200", description = "Recipe retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "ingredientId",
                    required = true,
                    description = "Ingredient ID"
            )
    })
    @GetMapping("/{recipeId}")
    ResponseEntity<Response> getRecipeById(@PathVariable("recipeId") Long recipeId);

    @Operation(
            summary = "Update recipe",
            description = "Updates recipe"
    )
    @ApiResponse(
            responseCode = "200", description = "Recipe updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "recipeId",
                    required = true,
                    description = "Recipe ID"
            ),
            @Parameter(
                    name = "updatedRecipeInfo",
                    required = true,
                    description = """
                            Updated recipe information \n
                            { \n
                                id: ignored
                                name: required \n
                                mainPhoto: not required \n
                                tagDTOs: { \n
                                    { \n
                                        id: not required, \n
                                    }, \n
                                    ... \n
                                }, \n
                                ingredientDTOs: { \n
                                    { \n
                                        name: required, \n
                                        amount: not required, \n
                                        measurementUnit: not required, \n
                                    }, \n
                                    ... \n
                                }, \n
                                instructions: not required \n
                                instructionPhotos: not required \n
                                links: not required \n
                            } \n
                            """
            )
    })
    @PutMapping("/{recipeId}")
    ResponseEntity<Response> updateRecipeById(@PathVariable("recipeId") Long recipeId,
                                              @RequestBody @Valid RecipeDTO updatedRecipeInfo);

    @Operation(
            summary = "Delete recipe",
            description = "Deletes recipe"
    )
    @ApiResponse(
            responseCode = "204", description = "Recipe deleted successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "recipeId",
                    required = true,
                    description = "Recipe ID"
            )
    })
    @DeleteMapping("/{recipeId}")
    ResponseEntity<Response> deleteRecipeById(@PathVariable("recipeId") Long recipeId);

    @Operation(
            summary = "Get all recipes by tag ID",
            description = """
                    Retrieves a list of recipes with a specific tag in the following format \\n
                    { \\n
                        id: Long,
                        name: String, \\n
                        mainPhoto: String, \\n
                        applicationUserId: Long, \\n
                        tagDTOs: {
                            { \\n
                                id: Long, \\n
                                name: String, \\n
                                icon: String, \\n
                                applicationUserId: Long \\n
                                recipeIds: { \\n
                                    Long, \\n
                                    ... \\n
                                } \\n
                            }, \\n
                            ... \\n
                        }, \\n
                        ingredientDTOs: { \\n
                            { \\n
                                id: Long, \\n
                                name: String, \\n
                                amount: Double, \\n
                                measurementUnit: MeasurementUnit (enum), \\n
                                applicationUserId: Long \\n
                            }, \\n
                            ... \\n
                        }, \\n
                        instructions: String \\n
                        instructionPhotos: {String...} \\n
                        links: {String...} \\n
                    } \\n
                    """
    )
    @ApiResponse(
            responseCode = "200", description = "List of recipes retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "tagId",
                    required = true,
                    description = "Tag ID"
            )
    })
    @GetMapping("/tagged/{tagId}")
    ResponseEntity<Response> getAllRecipesByTag(@PathVariable("tagId") Long tagId);
}
