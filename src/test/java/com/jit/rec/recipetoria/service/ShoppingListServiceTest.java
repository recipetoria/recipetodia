package com.jit.rec.recipetoria.service;

import com.github.javafaker.Faker;
import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.ApplicationUser;
import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.enumeration.MeasurementUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {

    private ShoppingListService underTest;
    @Mock
    private IngredientService ingredientService;
    @Mock
    private Authentication authentication;

    private final Faker FAKER = new Faker();
    private final Random RANDOM = new Random();

    @BeforeEach
    void setUp() {
        underTest = new ShoppingListService(ingredientService);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testGetAllIngredientsForShoppingList() {
        // Given
        Long ingredientId = RANDOM.nextLong(1, 1000);
        String name = FAKER.food().ingredient();
        Double amount = RANDOM.nextDouble(1, 1000);
        MeasurementUnit measurementUnit = MeasurementUnit.KILOGRAM;
        ApplicationUser applicationUser = new ApplicationUser();
        Recipe recipe = new Recipe();

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        ingredient.setName(name);
        ingredient.setAmount(amount);
        ingredient.setMeasurementUnit(measurementUnit);
        ingredient.setApplicationUser(applicationUser);
        ingredient.setRecipe(recipe);

        List<Ingredient> allIngredients = List.of(ingredient);
        when(ingredientService.getAllIngredientsByApplicationUser()).thenReturn(allIngredients);

        IngredientDTO ingredientDTO = new IngredientDTO(
                ingredientId,
                name,
                amount,
                measurementUnit,
                applicationUser.getId(),
                recipe.getId()
        );

        List<IngredientDTO> allIngredientDTOs = new ArrayList<>();
        allIngredientDTOs.add(ingredientDTO);

        // When
        List<IngredientDTO> actual = underTest.getAllIngredientsForShoppingList();

        // Then
        assertThat(allIngredientDTOs.size()).isEqualTo(actual.size());
        for (int i = 0; i < allIngredientDTOs.size(); i++) {
            assertThat(allIngredientDTOs.get(i)).isEqualTo(actual.get(i));
        }
    }

    @Test
    void testCreateIngredientInShoppingList() {
        // Given
        Long applicationUserId = RANDOM.nextLong(1, 1000);

        ApplicationUser applicationUser = ApplicationUser.builder()
                .id(applicationUserId)
                .build();
        when(authentication.getPrincipal()).thenReturn(applicationUser);


        String newIngredientDTOName = FAKER.food().ingredient();
        Double newIngredientDTOAmount = RANDOM.nextDouble(1, 1000);
        MeasurementUnit newIngredientDTOMeasurementUnit = MeasurementUnit.KILOGRAM;

        IngredientDTO newIngredientDTO = new IngredientDTO(
                null,
                newIngredientDTOName,
                newIngredientDTOAmount,
                newIngredientDTOMeasurementUnit,
                applicationUserId,
                null
        );

        Long createdIngredientId = RANDOM.nextLong(1, 1000);
        Recipe recipe = new Recipe();

        Ingredient createdIngredient = new Ingredient();
        createdIngredient.setId(createdIngredientId);
        createdIngredient.setName(newIngredientDTOName);
        createdIngredient.setAmount(newIngredientDTOAmount);
        createdIngredient.setMeasurementUnit(newIngredientDTOMeasurementUnit);
        createdIngredient.setApplicationUser(applicationUser);
        createdIngredient.setRecipe(recipe);

        when(ingredientService.createIngredient(newIngredientDTO, applicationUser)).thenReturn(createdIngredient);

        // When
        IngredientDTO actual = underTest.createIngredientInShoppingList(newIngredientDTO);

        // Then
        assertThat(actual.id()).isEqualTo(createdIngredientId);
        assertThat(actual.name()).isEqualTo(newIngredientDTOName);
        assertThat(actual.amount()).isEqualTo(newIngredientDTOAmount);
        assertThat(actual.measurementUnit()).isEqualTo(newIngredientDTOMeasurementUnit);
        assertThat(actual.applicationUserId()).isEqualTo(applicationUserId);
        assertThat(actual.recipeId()).isEqualTo(recipe.getId());
    }

    @Test
    void testDeleteAllIngredientsFromShoppingList() {
        // Arrange
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(RANDOM.nextLong(1, 1000));
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(RANDOM.nextLong(1, 1000));
        List<Ingredient> allIngredients = Arrays.asList(ingredient1, ingredient2);

        when(ingredientService.getAllIngredientsByApplicationUser()).thenReturn(allIngredients);

        // Act
        underTest.deleteAllIngredientsFromShoppingList();

        // Assert
        verify(ingredientService, times(1)).getAllIngredientsByApplicationUser();
        for (Ingredient oneIngredient : allIngredients) {
            verify(ingredientService, times(1)).deleteIngredientById(oneIngredient.getId());
        }
    }
}

