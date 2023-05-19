package com.jit.rec.recipetoria.service;

import com.github.javafaker.Faker;
import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.ApplicationUser;
import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.entity.Tag;
import com.jit.rec.recipetoria.enumeration.MeasurementUnit;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import com.jit.rec.recipetoria.repository.IngredientRepository;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    private IngredientService underTest;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private MessageSource messageSource;
    @Mock
    private Authentication authentication;

    private final Faker FAKER = new Faker();
    private final Random RANDOM = new Random();

    @BeforeEach
    void setUp() {
        underTest = new IngredientService(ingredientRepository, messageSource);
    }

    @Test
    void createIngredientInShoppingList() {
        // Given
        String newIngredientDTOName = FAKER.food().ingredient();
        Double newIngredientDTOAmount = RANDOM.nextDouble(1, 1000);
        MeasurementUnit newIngredientDTOMeasurementUnit = MeasurementUnit.KILOGRAM;

        IngredientDTO newIngredientDTO = new IngredientDTO(
                null,
                newIngredientDTOName,
                newIngredientDTOAmount,
                newIngredientDTOMeasurementUnit,
                null,
                null
        );

        Long applicationUserId = RANDOM.nextLong(1, 1000);
        ApplicationUserRole applicationUserRole = ApplicationUserRole.USER;
        String applicationUserEmail = FAKER.internet().emailAddress();
        String applicationUserPassword = FAKER.internet().password();
        String applicationUserName = FAKER.name().fullName();
        boolean applicationUserLocked = false; //TODO: change to true
        String applicationUserPhoto = FAKER.file().fileName(null, null, "jpg", null);
        List<Ingredient> applicationUserShoppingList = List.of(new Ingredient(), new Ingredient(), new Ingredient());
        List<Recipe> applicationUserRecipes = List.of(new Recipe(), new Recipe(), new Recipe());
        Set<Tag> applicationUserTags = Set.of(new Tag());

        ApplicationUser applicationUser = new ApplicationUser(
                applicationUserId,
                applicationUserRole,
                applicationUserEmail,
                applicationUserPassword,
                applicationUserName,
                applicationUserPhoto,
                applicationUserLocked,
                applicationUserShoppingList,
                applicationUserRecipes,
                applicationUserTags
        );

        Ingredient newRecipeIngredient = new Ingredient();
        newRecipeIngredient.setName(newIngredientDTO.name());
        newRecipeIngredient.setAmount(newIngredientDTO.amount());
        newRecipeIngredient.setMeasurementUnit(newIngredientDTO.measurementUnit());
        newRecipeIngredient.setApplicationUser(applicationUser);

        // When
        underTest.createIngredient(newIngredientDTO, applicationUser);

        // Then
        ArgumentCaptor<Ingredient> ingredientCaptor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(ingredientCaptor.capture());
        Ingredient capturedIngredient = ingredientCaptor.getValue();

        assertThat(capturedIngredient.getId()).isEqualTo(null);
        assertThat(capturedIngredient.getName()).isEqualTo(newIngredientDTOName);
        assertThat(capturedIngredient.getAmount()).isEqualTo(newIngredientDTOAmount);
        assertThat(capturedIngredient.getMeasurementUnit()).isEqualTo(newIngredientDTOMeasurementUnit);
        assertThat(capturedIngredient.getRecipe()).isEqualTo(null);
        assertThat(capturedIngredient.getApplicationUser()).isEqualTo(applicationUser);

        assertThat(capturedIngredient.getApplicationUser().getId()).isEqualTo(applicationUserId);
        assertThat(capturedIngredient.getApplicationUser().getApplicationUserRole()).isEqualTo(applicationUserRole);
        assertThat(capturedIngredient.getApplicationUser().getEmail()).isEqualTo(applicationUserEmail);
        assertThat(capturedIngredient.getApplicationUser().getPassword()).isEqualTo(applicationUserPassword);
        assertThat(capturedIngredient.getApplicationUser().getName()).isEqualTo(applicationUserName);
        assertThat(capturedIngredient.getApplicationUser().getPhoto()).isEqualTo(applicationUserPhoto);
        assertThat(capturedIngredient.getApplicationUser().isLocked()).isEqualTo(applicationUserLocked);
        assertThat(capturedIngredient.getApplicationUser().getShoppingList()).isEqualTo(applicationUserShoppingList);
        assertThat(capturedIngredient.getApplicationUser().getListOfRecipes()).isEqualTo(applicationUserRecipes);
        assertThat(capturedIngredient.getApplicationUser().getTags()).isEqualTo(applicationUserTags);
    }

    @Test
    void createIngredientInRecipe() {
        // Given
        String newIngredientDTOName = FAKER.food().ingredient();
        Double newIngredientDTOAmount = RANDOM.nextDouble(1, 1000);
        MeasurementUnit newIngredientDTOMeasurementUnit = MeasurementUnit.KILOGRAM;

        IngredientDTO newIngredientDTO = new IngredientDTO(
                null,
                newIngredientDTOName,
                newIngredientDTOAmount,
                newIngredientDTOMeasurementUnit,
                null,
                null
        );

        Long applicationUserId = RANDOM.nextLong(1, 1000);
        ApplicationUserRole applicationUserRole = ApplicationUserRole.USER;
        String applicationUserEmail = FAKER.internet().emailAddress();
        String applicationUserPassword = FAKER.internet().password();
        String applicationUserName = FAKER.name().fullName();
        boolean applicationUserLocked = false; //TODO: change to true
        String applicationUserPhoto = FAKER.file().fileName(null, null, "jpg", null);
        List<Ingredient> applicationUserShoppingList = List.of(new Ingredient(), new Ingredient(), new Ingredient());
        List<Recipe> applicationUserRecipes = List.of(new Recipe(), new Recipe(), new Recipe());
        Set<Tag> applicationUserTags = Set.of(new Tag());

        ApplicationUser applicationUser = new ApplicationUser(
                applicationUserId,
                applicationUserRole,
                applicationUserEmail,
                applicationUserPassword,
                applicationUserName,
                applicationUserPhoto,
                applicationUserLocked,
                applicationUserShoppingList,
                applicationUserRecipes,
                applicationUserTags
        );

        Long recipeId = RANDOM.nextLong(1, 1000);
        String recipeName = FAKER.food().dish();
        List<String> recipeInstructions = new ArrayList<>();
        for (int i = 0; i < RANDOM.nextInt(1, 20); i++) {
            recipeInstructions.add(FAKER.letterify("Step " + (i + 1) + ": ???????????????"));
        }
        List<String> recipeLinks = new ArrayList<>();
        for (int i = 0; i < RANDOM.nextInt(1, 20); i++) {
            recipeLinks.add(FAKER.internet().url());
        }
        List<String> recipeInstructionPhotos = new ArrayList<>();
        for (int i = 0; i < RANDOM.nextInt(1, 20); i++) {
            recipeInstructionPhotos.add(FAKER.file().fileName(null, null, "jpg", null));
        }
        String recipeMainPhoto = FAKER.file().fileName(null, null, "jpg", null);

        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setApplicationUser(applicationUser);
        recipe.setName(recipeName);
        recipe.setInstructions(recipeInstructions);
        recipe.setLinks(recipeLinks);
        recipe.setInstructionPhotos(recipeInstructionPhotos);
        recipe.setMainPhoto(recipeMainPhoto);

        Ingredient newRecipeIngredient = new Ingredient();
        newRecipeIngredient.setName(newIngredientDTOName);
        newRecipeIngredient.setAmount(newIngredientDTOAmount);
        newRecipeIngredient.setMeasurementUnit(newIngredientDTOMeasurementUnit);
        newRecipeIngredient.setRecipe(recipe);

        // When
        underTest.createIngredient(newIngredientDTO, recipe);

        // Then
        ArgumentCaptor<Ingredient> ingredientCaptor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(ingredientCaptor.capture());
        Ingredient capturedIngredient = ingredientCaptor.getValue();

        assertThat(capturedIngredient.getId()).isEqualTo(null);
        assertThat(capturedIngredient.getName()).isEqualTo(newIngredientDTOName);
        assertThat(capturedIngredient.getAmount()).isEqualTo(newIngredientDTOAmount);
        assertThat(capturedIngredient.getMeasurementUnit()).isEqualTo(newIngredientDTOMeasurementUnit);
        assertThat(capturedIngredient.getApplicationUser()).isEqualTo(null);
        assertThat(capturedIngredient.getRecipe()).isEqualTo(recipe);

        assertThat(capturedIngredient.getRecipe().getId()).isEqualTo(recipeId);
        assertThat(capturedIngredient.getRecipe().getApplicationUser()).isEqualTo(applicationUser);
        assertThat(capturedIngredient.getRecipe().getName()).isEqualTo(recipeName);
        assertThat(capturedIngredient.getRecipe().getInstructions()).isEqualTo(recipeInstructions);
        assertThat(capturedIngredient.getRecipe().getLinks()).isEqualTo(recipeLinks);
        assertThat(capturedIngredient.getRecipe().getInstructionPhotos()).isEqualTo(recipeInstructionPhotos);
        assertThat(capturedIngredient.getRecipe().getMainPhoto()).isEqualTo(recipeMainPhoto);
    }

    @Test
    void getIngredientById() {
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

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));

        // When
        IngredientDTO ingredientDTO = underTest.getIngredientById(ingredientId);

        // Then
        assertThat(ingredientDTO.id()).isEqualTo(ingredientId);
        assertThat(ingredientDTO.name()).isEqualTo(name);
        assertThat(ingredientDTO.amount()).isEqualTo(amount);
        assertThat(ingredientDTO.measurementUnit()).isEqualTo(measurementUnit);
        assertThat(ingredientDTO.applicationUserId()).isEqualTo(applicationUser.getId());
        assertThat(ingredientDTO.recipeId()).isEqualTo(recipe.getId());
    }

    @Test
    void getIngredientByIdException() {
        // Given
        Long ingredientId = RANDOM.nextLong(1, 1000);

        // Given
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.empty());
        when(messageSource.getMessage("exception.ingredient.notFound", null, Locale.getDefault()))
                .thenReturn(FAKER.letterify("?????"));

        // When
        // Then
        assertThatThrownBy(() -> underTest.getIngredientById(ingredientId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messageSource.getMessage("exception.ingredient.notFound", null, Locale.getDefault()));
    }

    @Test
    void updateIngredientById() {
        // Given
        Long ingredientId = RANDOM.nextLong(1, 1000);

        String newIngredientDTOName = FAKER.food().ingredient();
        Double newIngredientDTOAmount = RANDOM.nextDouble(1, 1000);
        MeasurementUnit newIngredientDTOMeasurementUnit = MeasurementUnit.KILOGRAM;

        IngredientDTO updatedIngredientInfo = new IngredientDTO(
                null,
                newIngredientDTOName,
                newIngredientDTOAmount,
                newIngredientDTOMeasurementUnit,
                null,
                null
        );

        Long applicationUserId = RANDOM.nextLong(1, 1000);
        ApplicationUser ingredientToBeUpdatedApplicationUser = new ApplicationUser();
        ingredientToBeUpdatedApplicationUser.setId(applicationUserId);

        Recipe ingredientToBeUpdatedRecipe = new Recipe();

        Ingredient ingredientToBeUpdated = new Ingredient();
        ingredientToBeUpdated.setId(ingredientId);
        ingredientToBeUpdated.setName(newIngredientDTOName);
        ingredientToBeUpdated.setAmount(newIngredientDTOAmount);
        ingredientToBeUpdated.setMeasurementUnit(newIngredientDTOMeasurementUnit);
        ingredientToBeUpdated.setApplicationUser(ingredientToBeUpdatedApplicationUser);
        ingredientToBeUpdated.setRecipe(ingredientToBeUpdatedRecipe);

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredientToBeUpdated));

        // When
        underTest.updateIngredientById(ingredientId, updatedIngredientInfo);

        // Then
        ArgumentCaptor<Ingredient> ingredientCaptor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(ingredientCaptor.capture());
        Ingredient capturedIngredient = ingredientCaptor.getValue();

        IngredientDTO actual = IngredientDTO.convertToDTO(capturedIngredient);
        assertThat(actual.id()).isEqualTo(ingredientId);
        assertThat(actual.name()).isEqualTo(newIngredientDTOName);
        assertThat(actual.amount()).isEqualTo(newIngredientDTOAmount);
        assertThat(actual.measurementUnit()).isEqualTo(newIngredientDTOMeasurementUnit);
        assertThat(actual.applicationUserId()).isEqualTo(applicationUserId);
        assertThat(actual.recipeId()).isEqualTo(ingredientToBeUpdatedRecipe.getId());
    }

    @Test
    void updateIngredientByIdException() {
        // Given
        Long ingredientId = RANDOM.nextLong(1, 1000);

        String newIngredientDTOName = FAKER.food().ingredient();
        Double newIngredientDTOAmount = RANDOM.nextDouble(1, 1000);
        MeasurementUnit newIngredientDTOMeasurementUnit = MeasurementUnit.KILOGRAM;

        IngredientDTO updatedIngredientInfo = new IngredientDTO(
                null,
                newIngredientDTOName,
                newIngredientDTOAmount,
                newIngredientDTOMeasurementUnit,
                null,
                null
        );

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.empty());
        when(messageSource.getMessage("exception.ingredient.notFound", null, Locale.getDefault()))
                .thenReturn(FAKER.letterify("?????"));

        // When
        assertThatThrownBy(() -> underTest.updateIngredientById(ingredientId, updatedIngredientInfo))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messageSource.getMessage("exception.ingredient.notFound", null, Locale.getDefault()));

        // Then
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void deleteIngredientById() {
        // Given
        Long ingredientId = RANDOM.nextLong(1, 1000);
        when(ingredientRepository.existsById(ingredientId)).thenReturn(true);

        // When
        underTest.deleteIngredientById(ingredientId);

        // Then
        verify(ingredientRepository).deleteById(ingredientId);
    }

    @Test
    void deleteIngredientByIdException() {
        // Given
        Long ingredientId = RANDOM.nextLong(1, 1000);
        when(ingredientRepository.existsById(ingredientId)).thenReturn(false);
        when(messageSource.getMessage("exception.ingredient.notFound", null, Locale.getDefault()))
                .thenReturn(FAKER.letterify("?????"));

        // When
        assertThatThrownBy(() -> underTest.deleteIngredientById(ingredientId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messageSource.getMessage("exception.ingredient.notFound", null, Locale.getDefault()));

        // Then
        verify(ingredientRepository, never()).deleteById(ingredientId);
    }

    @Test
    void getAllIngredientsByApplicationUser() {
        // Given
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long applicationUserId = RANDOM.nextLong(1, 1000);
        ApplicationUserRole applicationUserRole = ApplicationUserRole.USER;
        String applicationUserEmail = FAKER.internet().emailAddress();
        String applicationUserPassword = FAKER.internet().password();
        String applicationUserName = FAKER.name().fullName();
        boolean applicationUserLocked = false; //TODO: change to true
        String applicationUserPhoto = FAKER.file().fileName(null, null, "jpg", null);
        List<Ingredient> applicationUserShoppingList = List.of(new Ingredient(), new Ingredient(), new Ingredient());
        List<Recipe> applicationUserRecipes = List.of(new Recipe(), new Recipe(), new Recipe());
        Set<Tag> applicationUserTags = Set.of(new Tag());

        ApplicationUser applicationUser = new ApplicationUser(
                applicationUserId,
                applicationUserRole,
                applicationUserEmail,
                applicationUserPassword,
                applicationUserName,
                applicationUserPhoto,
                applicationUserLocked,
                applicationUserShoppingList,
                applicationUserRecipes,
                applicationUserTags
        );
        when(authentication.getPrincipal()).thenReturn(applicationUser);

        List<Ingredient> ingredients = new ArrayList<>();
        when(ingredientRepository.findAllByApplicationUser(applicationUser)).thenReturn(ingredients);

        // When
        List<Ingredient> actual = underTest.getAllIngredientsByApplicationUser();

        // Then
        verify(ingredientRepository).findAllByApplicationUser(applicationUser);
        assertThat(ingredients).isEqualTo(actual);

        assertThat(applicationUser.getId()).isEqualTo(applicationUserId);
        assertThat(applicationUser.getApplicationUserRole()).isEqualTo(applicationUserRole);
        assertThat(applicationUser.getEmail()).isEqualTo(applicationUserEmail);
        assertThat(applicationUser.getPassword()).isEqualTo(applicationUserPassword);
        assertThat(applicationUser.getName()).isEqualTo(applicationUserName);
        assertThat(applicationUser.getPhoto()).isEqualTo(applicationUserPhoto);
        assertThat(applicationUser.isLocked()).isEqualTo(applicationUserLocked);
        assertThat(applicationUser.getShoppingList()).isEqualTo(applicationUserShoppingList);
        assertThat(applicationUser.getListOfRecipes()).isEqualTo(applicationUserRecipes);
        assertThat(applicationUser.getTags()).isEqualTo(applicationUserTags);
    }
}