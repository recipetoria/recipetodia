package com.jit.rec.recipetoria.recipe;

import com.jit.rec.recipetoria.filestorage.FileStorageService;
import com.jit.rec.recipetoria.ingredient.IngredientDTO;
import com.jit.rec.recipetoria.ingredient.IngredientDTOMapper;
import com.jit.rec.recipetoria.tag.Tag;
import com.jit.rec.recipetoria.tag.TagDTO;
import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.ingredient.Ingredient;
import com.jit.rec.recipetoria.exception.ResourceNotFoundException;
import com.jit.rec.recipetoria.ingredient.IngredientService;
import com.jit.rec.recipetoria.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final TagService tagService;
    private final RecipeDTOMapper recipeDTOMapper;
    private final IngredientDTOMapper ingredientDTOMapper;
    private final FileStorageService fileStorageService;
    private final MessageSource messageSource;

    public List<RecipeDTO> getAllRecipes() {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Recipe> allRecipes = recipeRepository.findAllByApplicationUser(applicationUser);

        List<RecipeDTO> allRecipesDTOs = new ArrayList<>();
        for (Recipe oneRecipe : allRecipes) {
            allRecipesDTOs.add(recipeDTOMapper.apply(oneRecipe));
        }

        return allRecipesDTOs;
    }

    public RecipeDTO createRecipe(RecipeDTO newRecipeDTO, ApplicationUser newApplicationUser) {
        Recipe newRecipe = new Recipe();

        if (newApplicationUser == null) {
            ApplicationUser applicationUser =
                    (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            newRecipe.setApplicationUser(applicationUser);
        } else {
            newRecipe.setApplicationUser(newApplicationUser);
        }

        newRecipe.setName(newRecipeDTO.name());
        newRecipe.setInstructions(newRecipeDTO.instructions());
        newRecipe.setLinks(newRecipeDTO.links());

        Optional.ofNullable(newRecipeDTO.tagDTOs())
                .stream()
                .flatMap(Collection::stream)
                .map(TagDTO::id)
                .map(tagService::getTagById)
                .forEach(newRecipe.getTags()::add);

        newRecipe = recipeRepository.save(newRecipe);

        if (newRecipeDTO.ingredientDTOs() != null) {
            for (IngredientDTO ingredientDTO : newRecipeDTO.ingredientDTOs()) {
                Ingredient recipeIngredient = ingredientService.createIngredient(ingredientDTO, newRecipe);
                newRecipe.getIngredientList().add(recipeIngredient);
            }
        }

        return recipeDTOMapper.apply(newRecipe);
    }

    public RecipeDTO getRecipeDTOById(Long recipeId) {
        return recipeDTOMapper.apply(getRecipeById(recipeId));
    }

    public void updateRecipeById(Long recipeId, RecipeDTO updatedRecipeInfo, MultipartFile mainPhoto, MultipartFile instructionPhoto) {
        Recipe recipeToBeUpdated = getRecipeById(recipeId);

        if (updatedRecipeInfo != null) {
            updateRecipeInfo(recipeToBeUpdated, updatedRecipeInfo);
        }
        if (mainPhoto != null) {
            updateRecipeMainPhoto(recipeToBeUpdated, mainPhoto);
        }
        if (instructionPhoto != null) {
            addRecipeInstructionPhoto(recipeToBeUpdated, instructionPhoto);
        }

        recipeRepository.save(recipeToBeUpdated);
    }

    public void deleteInstructionPhoto(Long recipeId, RecipeDTO recipeDTO) {
        if (recipeDTO.instructionPhotos() != null) {
            String instructionPhoto = recipeDTO.instructionPhotoToDelete();
            if (getRecipeById(recipeId).getInstructionPhotos().contains(instructionPhoto)) {

                fileStorageService.deletePhoto(instructionPhoto);

                Recipe recipeToBeUpdated = getRecipeById(recipeId);
                recipeToBeUpdated.getInstructionPhotos().remove(instructionPhoto);

                recipeRepository.save(recipeToBeUpdated);
            }
        }
    }

    public void setInstructionPhotoAsRecipeMainPhoto(Long recipeId, int instructionPhotoSeqNo) {
        Recipe recipe = getRecipeById(recipeId);

        String newPhotoName = recipe.getInstructionPhotos().get(instructionPhotoSeqNo);
        byte[] newPhotoBytes = fileStorageService.getPhoto(newPhotoName);
        updateRecipeMainPhoto(recipe, newPhotoBytes, newPhotoName);

        recipeRepository.save(recipe);
    }

    public void deleteRecipeById(Long recipeId) {
        getRecipeById(recipeId);
        recipeRepository.deleteById(recipeId);
    }

    public List<RecipeDTO> getAllRecipesByTag(Long tagId) {
        List<Recipe> recipesByTag = recipeRepository.findByTagsId(tagId);

        List<RecipeDTO> recipeDTOs = new ArrayList<>();
        for (Recipe oneRecipe : recipesByTag) {

            byte[] mainPhoto = "".getBytes();
            if (oneRecipe.getMainPhoto() != null) {
                mainPhoto = fileStorageService.getPhoto(oneRecipe.getMainPhoto());
            }

            RecipeDTO recipeDTO = new RecipeDTO(
                    oneRecipe.getId(),
                    oneRecipe.getName(),
                    mainPhoto,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            recipeDTOs.add(recipeDTO);
        }

        return recipeDTOs;
    }

    public byte[] getRecipeMainPhoto(Long recipeId) {
        Recipe recipe = getRecipeById(recipeId);

        if (StringUtils.isBlank(recipe.getMainPhoto())) {
            throw new ResourceNotFoundException(messageSource.getMessage(
                    "exception.recipe.getMainPhoto.notFound", null, Locale.getDefault()));
        }

        return fileStorageService.getPhoto(recipe.getMainPhoto());
    }

    public List<byte[]> getRecipeInstructionPhotos(Long recipeId) {
        Recipe recipe = getRecipeById(recipeId);

        if (recipe.getInstructionPhotos() == null) {
            throw new ResourceNotFoundException(messageSource.getMessage(
                    "exception.recipe.getInstructionPhotos.notFound", null, Locale.getDefault()));
        }

        List<byte[]> instructionPhotos = new ArrayList<>();
        for (String oneInstructionPhoto : recipe.getInstructionPhotos()) {
            instructionPhotos.add(fileStorageService.getPhoto(oneInstructionPhoto));
        }

        return instructionPhotos;
    }

    public void addIngredientFromRecipeToShoppingList(Long recipeId, Long ingredientId) {
        Recipe recipe = getRecipeById(recipeId);

        recipe.getIngredientList()
                .stream()
                .filter(ingredient -> Objects.equals(ingredient.getId(), ingredientId))
                .findFirst().ifPresent(recipeIngredient ->
                        ingredientService.createIngredient(ingredientDTOMapper.apply(recipeIngredient)));
    }

    private Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.recipe.notFound", null, Locale.getDefault())));
    }

    private void updateRecipeInfo(Recipe recipeToBeUpdated, RecipeDTO updatedRecipeInfo) {
        if (updatedRecipeInfo.name() != null) {
            recipeToBeUpdated.setName(updatedRecipeInfo.name());
        }
        if (updatedRecipeInfo.tagDTOs() != null) {
            recipeToBeUpdated.setTags(new ArrayList<>());
            for (TagDTO newTagDTO : updatedRecipeInfo.tagDTOs()) {
                Tag newTag = tagService.getTagById(newTagDTO.id());
                recipeToBeUpdated.getTags().add(newTag);
            }
        }
        if (updatedRecipeInfo.ingredientDTOs() != null) {
            for (Ingredient ingredient : recipeToBeUpdated.getIngredientList()) {
                recipeToBeUpdated.setIngredientList(new ArrayList<>());
                ingredientService.deleteIngredientById(ingredient.getId());
            }
            recipeToBeUpdated.setIngredientList(new ArrayList<>());
            for (IngredientDTO newIngredientDTO : updatedRecipeInfo.ingredientDTOs()) {
                Ingredient ingredient = new Ingredient();

                ingredient.setName(newIngredientDTO.name());
                ingredient.setAmount(newIngredientDTO.amount());
                ingredient.setMeasurementUnit(newIngredientDTO.measurementUnit());
                ingredient.setRecipe(recipeToBeUpdated);

                recipeToBeUpdated.getIngredientList().add(ingredient);
            }
        }
        if (updatedRecipeInfo.instructions() != null) {
            recipeToBeUpdated.setInstructions(updatedRecipeInfo.instructions());
        }
        if (updatedRecipeInfo.links() != null) {
            recipeToBeUpdated.setLinks(updatedRecipeInfo.links());
        }
    }

    private void updateRecipeMainPhoto(Recipe recipeToBeUpdated, MultipartFile file) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        fileStorageService.validatePhoto(file);

        try {
            deleteRecipeMainPhoto(recipeToBeUpdated);
            String recipeMainPhotoPath = fileStorageService.putRecipeMainPhoto(
                    applicationUser.getId(), recipeToBeUpdated.getId(), file.getBytes(), file.getOriginalFilename());
            recipeToBeUpdated.setMainPhoto(recipeMainPhotoPath);
        } catch (IOException e) {
            throw new RuntimeException(messageSource.getMessage(
                    "exception.recipe.updateRecipeById.notUploaded", null, Locale.getDefault()));
        }
    }

    private void updateRecipeMainPhoto(Recipe recipeToBeUpdated, byte[] fileBytes, String fileName) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        deleteRecipeMainPhoto(recipeToBeUpdated);
        String recipeMainPhotoPath = fileStorageService.putRecipeMainPhoto(
                applicationUser.getId(), recipeToBeUpdated.getId(), fileBytes, fileName);
        recipeToBeUpdated.setMainPhoto(recipeMainPhotoPath);
    }

    private void deleteRecipeMainPhoto(Recipe recipeToBeUpdated) {
        fileStorageService.deletePhoto(recipeToBeUpdated.getMainPhoto());
        recipeToBeUpdated.setMainPhoto(null);
        recipeRepository.save(recipeToBeUpdated);
    }

    private void addRecipeInstructionPhoto(Recipe recipeToBeUpdated, MultipartFile file) {
        ApplicationUser applicationUser =
                (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        fileStorageService.validatePhoto(file);

        int instructionPhotoUniqueNumber = generateInstructionPhotoUniqueNumber(recipeToBeUpdated);

        try {
            String recipeInstructionPhotoPath = fileStorageService.putRecipeInstructionPhoto(
                    applicationUser.getId(), recipeToBeUpdated.getId(), file.getBytes(), file.getOriginalFilename(), instructionPhotoUniqueNumber);
            recipeToBeUpdated.getInstructionPhotos().add(recipeInstructionPhotoPath);
        } catch (IOException e) {
            throw new RuntimeException(messageSource.getMessage(
                    "exception.recipe.updateRecipeById.notUploaded", null, Locale.getDefault()));
        }
    }

    private int generateInstructionPhotoUniqueNumber(Recipe recipe) {
        Set<Integer> usedUniqueNumbers = new HashSet<>();

        for (String oneInstructionPhoto : recipe.getInstructionPhotos()) {
            String fileNameWithoutExtension = oneInstructionPhoto.substring(0, oneInstructionPhoto.lastIndexOf('.'));
            String[] parts = fileNameWithoutExtension.split("-");
            int oneInstructionPhotoUniqueNumber = Integer.parseInt(parts[parts.length - 1]);
            usedUniqueNumbers.add(oneInstructionPhotoUniqueNumber);
        }
        int instructionPhotoUniqueNumber;
        do {
            instructionPhotoUniqueNumber = new Random().nextInt(0, 100);
        } while (usedUniqueNumbers.contains(instructionPhotoUniqueNumber));

        return instructionPhotoUniqueNumber;
    }
}
