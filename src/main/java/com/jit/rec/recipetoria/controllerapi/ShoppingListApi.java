package com.jit.rec.recipetoria.controllerapi;

import com.jit.rec.recipetoria.dto.IngredientDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Shopping List")
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
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "User has no access to the resource",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Resource id was not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public interface ShoppingListApi {

    @Operation(
            summary = "Get all ingredients in the Shopping List",
            description = "Retrieves a list of all ingredients in the Shopping List"
    )
    @ApiResponse(
            responseCode = "200", description = "List of ingredients in the Shopping List retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @GetMapping
    ResponseEntity<Response> getAllIngredientsForShoppingList();

    @Operation(
            summary = "Create new ingredient",
            description = "Creates a new ingredient and adds it to the Shopping List"
    )
    @ApiResponse(
            responseCode = "201", description = "Ingredient created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "newIngredientInfo",
                    required = true,
                    description = """
                            New ingredient information \n
                            id: ignored \n
                            name: required \n
                            amount: not required \n
                            measurementUnit: not required \n
                            applicationUserId: ignored \n
                            """
            )
    })
    @PostMapping
    ResponseEntity<Response> createIngredientInShoppingList(IngredientDTO newIngredientInfo);

    @Operation(
            summary = "Remove ingredients from Shopping List",
            description = "Removes ingredients from Shopping List"
    )
    @ApiResponse(
            responseCode = "204", description = "Shopping List emptied successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @DeleteMapping
    ResponseEntity<Response> deleteAllIngredientsFromShoppingList();
}
