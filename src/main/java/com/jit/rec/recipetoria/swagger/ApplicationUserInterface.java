package com.jit.rec.recipetoria.swagger;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Shopping List")
@ApiResponses({
        @ApiResponse(
                responseCode = "400", description = "Invalid data sent to the server",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = com.jit.rec.recipetoria.entity.ApiResponse.class)
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
public interface ApplicationUserInterface {

    @Operation(
            operationId = "getAllIngredients",
            summary = "Get all ingredients in the Shopping List",
            description = "Retrieves a list of all ingredients in the Shopping List"
    )
    @ApiResponse(
            responseCode = "200", description = "List of ingredients in the Shopping List retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @GetMapping
    ResponseEntity<com.jit.rec.recipetoria.entity.ApiResponse> getAllIngredients();

    @Operation(
            operationId = "createIngredient",
            summary = "Create new ingredient",
            description = "Creates a new ingredient and adds it to the Shopping List"
    )
    @ApiResponse(
            responseCode = "201", description = "Ingredient created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @PostMapping
    ResponseEntity<com.jit.rec.recipetoria.entity.ApiResponse> createIngredient(
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
            IngredientDTO newIngredientInfo);
}
