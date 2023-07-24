package com.jit.rec.recipetoria.recipe;

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
@RequestMapping("/api/v1/client/recipes")
@RequiredArgsConstructor
public class RecipeController implements RecipeApi {

    private final RecipeService recipeService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<Response> getAllRecipes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.recipe.getAllRecipes", null, Locale.getDefault()))
                        .data(Map.of("allRecipesDTOs", recipeService.getAllRecipes()))
                        .build());
    }

    @PostMapping
    public ResponseEntity<Response> createRecipe(@RequestBody @Valid RecipeDTO newRecipeInfo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .message(messageSource.getMessage(
                                "response.recipe.createRecipe", null, Locale.getDefault()))
                        .data(Map.of("createdRecipeDTO", recipeService.createRecipe(newRecipeInfo)))
                        .build());
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Response> getRecipeById(@PathVariable("recipeId") Long recipeId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.recipe.getRecipeById", null, Locale.getDefault()))
                        .data(Map.of("recipeDTO", recipeService.getRecipeDTOById(recipeId)))
                        .build());
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Response> updateRecipeInfoById(@PathVariable("recipeId") Long recipeId,
                                                         @RequestBody @Valid RecipeDTO updatedRecipeInfo) {
        recipeService.updateRecipeById(recipeId, updatedRecipeInfo, null, null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.recipe.updateRecipeById", null, Locale.getDefault()))
                        .build());
    }

    @PutMapping("/{recipeId}/main-photo")
    public ResponseEntity<Response> updateRecipeMainPhotoById(@PathVariable("recipeId") Long recipeId,
                                                              @RequestBody MultipartFile file) {
        recipeService.updateRecipeById(recipeId, null, file, null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage("response.recipe.updateRecipeById", null, Locale.getDefault()))
                        .build());
    }

    @GetMapping("/{recipeId}/main-photo")
    public ResponseEntity<Response> getRecipeMainPhoto(@PathVariable("recipeId") Long recipeId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.recipe.getRecipeMainPhoto", null, Locale.getDefault()))
                        .data(Map.of("recipeMainPhoto", recipeService.getRecipeMainPhoto(recipeId)))
                        .build());
    }

    @PutMapping("/{recipeId}/instruction-photo")
    public ResponseEntity<Response> addRecipeInstructionPhoto(@PathVariable("recipeId") Long recipeId,
                                                              @RequestBody MultipartFile file) {
        recipeService.updateRecipeById(recipeId, null, null, file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage("response.recipe.addRecipeInstructionPhotos", null, Locale.getDefault()))
                        .build());
    }

    @GetMapping("/{recipeId}/instruction-photos")
    public ResponseEntity<Response> getRecipeInstructionPhotos(@PathVariable("recipeId") Long recipeId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.recipe.getRecipeInstructionPhotos", null, Locale.getDefault()))
                        .data(Map.of("recipeInstructionPhotos", recipeService.getRecipeInstructionPhotos(recipeId)))
                        .build());
    }

    @DeleteMapping("/{recipeId}/instruction-photo")
    public ResponseEntity<Response> deleteRecipeInstructionPhoto(@PathVariable("recipeId") Long recipeId,
                                                                 @RequestBody RecipeDTO recipeDTO) {
        recipeService.deleteInstructionPhoto(recipeId, recipeDTO);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message(messageSource.getMessage(
                                "response.recipe.deleteRecipeInstructionPhoto", null, Locale.getDefault()))
                        .build());
    }

    @GetMapping("/{recipeId}/instruction-photos/{instructionPhotoSeqNo}/set-main-photo")
    public ResponseEntity<Response> setInstructionPhotoAsRecipeMainPhoto(@PathVariable("recipeId") Long recipeId,
                                                                         @PathVariable("instructionPhotoSeqNo") int instructionPhotoSeqNo) {
        recipeService.setInstructionPhotoAsRecipeMainPhoto(recipeId, instructionPhotoSeqNo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage("response.recipe.setInstructionPhotoAsRecipeMainPhoto", null, Locale.getDefault()))
                        .build());
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Response> deleteRecipeById(@PathVariable("recipeId") Long recipeId) {
        recipeService.deleteRecipeById(recipeId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message(messageSource.getMessage(
                                "response.recipe.deleteRecipeById", null, Locale.getDefault()))
                        .build());
    }

    @GetMapping("/tagged/{tagId}")
    public ResponseEntity<Response> getAllRecipesByTag(@PathVariable("tagId") Long tagId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.recipe.getAllRecipesByTag", null, Locale.getDefault()))
                        .data(Map.of("allRecipesByTag", recipeService.getAllRecipesByTag(tagId)))
                        .build());
    }

    @PostMapping("{recipeId}/ingredient/{ingredientId}")
    public ResponseEntity<Response> addIngredientFromRecipeToShoppingList(@PathVariable("recipeId") Long recipeId,
                                                                          @PathVariable("ingredientId") Long ingredientId) {
        recipeService.addIngredientFromRecipeToShoppingList(recipeId, ingredientId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .message(messageSource.getMessage(
                                "response.recipe.addIngredientFromRecipeToShoppingList", null, Locale.getDefault()))
                        .build());
    }
}
