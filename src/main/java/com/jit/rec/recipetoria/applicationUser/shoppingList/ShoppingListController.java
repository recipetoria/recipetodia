package com.jit.rec.recipetoria.applicationUser.shoppingList;

import com.jit.rec.recipetoria.ingredient.IngredientDTO;
import com.jit.rec.recipetoria.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ShoppingListController implements ShoppingListApi {

    private final ShoppingListService shoppingListService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<Response> getAllIngredientsForShoppingList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .message(messageSource.getMessage(
                                "response.user.getAllIngredients", null, Locale.getDefault()))
                        .data(Map.of("allIngredientDTOs", shoppingListService.getAllIngredientsForShoppingList()))
                        .build());
    }

    @PostMapping
    public ResponseEntity<Response> createIngredientInShoppingList(@Valid @RequestBody IngredientDTO newIngredientInfo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .message(messageSource.getMessage(
                                "response.user.createIngredient", null, Locale.getDefault()))
                        .data(Map.of("createdIngredientDTO",
                                shoppingListService.createIngredientInShoppingList(newIngredientInfo)))
                        .build());
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteAllIngredientFromShoppingList() {
        shoppingListService.deleteAllIngredientFromShoppingList();

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message(messageSource.getMessage(
                                "response.ingredient.deleteAllIngredientsFromShL", null, Locale.getDefault()))
                        .build());
    }
}
