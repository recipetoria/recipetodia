package com.jit.rec.recipetoria.controllerapi;

import com.jit.rec.recipetoria.dto.RecipeDTO;
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

    @Operation(summary = "Get all recipes of current ApplicationUser",
            description = """
                    send: \n
                    response: \n
                    array of RecipeDTO objects with array of TagDTO and array IgredientDTO inside: \n
                        { \n
                          "name": "recipe", \n
                          "mainPhoto": "photoUrl", \n
                          "tagDTOs": --array of TagDTOs \n
                                    "id": 5, \n
                                    "name": "tag3", \n
                                    "icon": iconUrl, \n
                                    "applicationUserId": 1,  --owner \n
                                    "recipeIds": [] --ids of recipes tagged with this tag \n
                          "ingredientDTOs": --array of IngredientDTOs \n
                                   "id": 0, \n
                                   "name": "string", \n
                                   "amount": 0, \n
                                   "measurementUnit": "KG", \n
                                   "applicationUserId": null  --owner of ingredient must be null in recipe \n
                          "instructions": --array of Strings \n
                                    "take a cucumber", \n
                                    "wash", \n
                                    "eat" \n
                          "instructionPhotos":  --array of Strings \n
                                  "photoUrl" \n
                          "links": --array of Strings \n
                                    "url" \n
                        } \n
                        
                    """)
    ResponseEntity<Response> getAllRecipes();

    @Operation(summary = "Create a new recipe",
            description = """
                    required info to create a recipe is name, \n
                    required info to create an ingredient is name \n
                    send: \n
                          "name": "string",  --REQUIRED \n
                          "mainPhoto": "string", --optional \n
                          "tagDTOs": --array of TagDTOs but the only field needed is id of existing tag \n
                              "id": 0  --optional \n
                          "ingredientDTOs": --array of IngredientDTOs with name(required), amoung, measurement \n
                              "name": "string", --REQUIRED for creating an ingredient (if you want to add some) \n
                              "amount": 0, --optional \n
                              "measurementUnit": "KG", --optional \n
                          "instructions":  --array. optional \n
                            "string" \n
                          "instructionPhotos":  -- array. optional \n
                            "string" \n
                          "links": -- array. optional \n
                            "string" \n
                    response: the whole recipe with all fields filled with data, 0 or null \n
                          RecipeDTO
                              "id": 0, \n
                              "name": "string", \n
                              "applicationUserId": 0, --owner \n
                              "mainPhoto": "string", \n
                              "tagDTOs":  --array or TagDTOs \n
                                  "id": 0, \n
                                  "name": "string", \n
                                  "icon": "string", \n
                                  "applicationUserId": 0, \n
                                  "recipeIds": [ ]--array of ids of recipes tagged with this tag \n
                              "ingredientDTOs":  -- array of Ingredient DTOs \n
                                  "id": 0, \n
                                  "name": "string", \n
                                  "amount": 0, \n
                                  "measurementUnit": "KG", \n
                                  "applicationUserId": 0 --must be null in ingredients in recipe \n
                              "instructions":  --array of strings \n
                                  "string" \n
                              "instructionPhotos": [ --array of strings \n
                                   "string" \n
                              "links": [--array of strings \n
                                   "string" \n
                    """)
    ResponseEntity<Response> createRecipe(RecipeDTO newRecipeInfo);

    @Operation(summary = "Get recipe by id",
            description = """
                    send: id in url in \n
                    response: the whole RecipeDTO with arrays of TagDTO, InfredientDTO, Strings \n
                    all fields are filled with data, 0 or null. \n
                    Ingredient IDs in recipe ingredients must be null \n
                    """)
    ResponseEntity<Response> getRecipeById(Long recipeId);

    @Operation(summary = "Update recipe by id",
            description = """
                    send: \n
                    required info to update a recipe is name, \n
                    required info to update an ingredient is name \n
                    send: \n
                    { \n
                          "name": "string",  --REQUIRED \n
                          "mainPhoto": "string", --optional \n
                          "tagDTOs": [ \n
                            { \n
                              "id": 0  --optional \n
                            } \n
                          ], \n
                          "ingredientDTOs": [ \n
                            { \n
                              "name": "string", --REQUIRED for creating an ingredient (if you want to add some) \n
                              "amount": 0, --optional \n
                              "measurementUnit": "KG", --optional \n
                            } \n
                          ], \n
                          "instructions": [ --optional \n
                            "string" \n
                          ], \n
                          "instructionPhotos": [ --optional \n
                            "string" \n
                          ], \n
                          "links": [ --optional \n
                            "string" \n
                          ] \n
                    response: the whole recipe with all fields filled with data, 0 or null \n
                            { \n
                              "id": 0, \n
                              "name": "string", \n
                              "applicationUserId": 0, --owner \n
                              "mainPhoto": "string", \n
                              "tagDTOs": [ --array or TagDTOs \n
                                { \n
                                  "id": 0, \n
                                  "name": "string", \n
                                  "icon": "string", \n
                                  "applicationUserId": 0, \n
                                  "recipeIds": [ --array of ids of recipes tagged with this tag \n
                                    0 \n
                                  ] \n
                                } \n
                              ], \n
                              "ingredientDTOs": [ -- array of Ingredient DTOs \n
                                { \n
                                  "id": 0, \n
                                  "name": "string", \n
                                  "amount": 0, \n
                                  "measurementUnit": "KG", \n
                                  "applicationUserId": 0 --must be null in ingredients in recipe \n
                                } \n
                              ], \n
                              "instructions": [ --array of strings \n
                                "string" \n
                              ], \n
                              "instructionPhotos": [ --array of strings \n
                                "string" \n
                              ], \n
                              "links": [--array of strings \n
                                "string" \n
                              ] \n
                            } \n
                        } \n
                    !!!!
                    """)
    ResponseEntity<Response> updateRecipeById(Long recipeId,
                                              RecipeDTO updatedRecipeInfo);

    @Operation(summary = "Delete recipe by id",
            description = """
                    send: id in url \n
                    response: status 200(ok) \n
                    """)
    ResponseEntity<Response> deleteRecipeById(Long recipeId);
}
