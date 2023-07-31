package com.jit.rec.recipetoria.security.auth;

import com.jit.rec.recipetoria.exception.EmailAlreadyExistsException;
import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserRepository;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserRole;
import com.jit.rec.recipetoria.filestorage.FileStorageService;
import com.jit.rec.recipetoria.ingredient.IngredientDTO;
import com.jit.rec.recipetoria.ingredient.MeasurementUnit;
import com.jit.rec.recipetoria.recipe.RecipeDTO;
import com.jit.rec.recipetoria.recipe.RecipeService;
import com.jit.rec.recipetoria.security.jwt.JwtService;
import com.jit.rec.recipetoria.tag.TagDTO;
import com.jit.rec.recipetoria.tag.TagService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final RecipeService recipeService;
    private final TagService tagService;
    private final FileStorageService fileStorageService;

    public AuthenticationResponse register(AuthenticationRequest authenticationRequest) {
        checkIfEmailExists(authenticationRequest.email());

        ApplicationUser applicationUser = ApplicationUser.builder()
                .name(authenticationRequest.name())
                .email(authenticationRequest.email())
                .password(passwordEncoder.encode(authenticationRequest.password()))
                .applicationUserRole(ApplicationUserRole.USER)
//                .locked(true) //TODO: uncomment
                .build();

        String jwtToken = jwtService.generateToken(applicationUser);

//        sendEmail(applicationUser, jwtToken); //TODO: uncomment

        ApplicationUser newApplicationUser = applicationUserRepository.save(applicationUser);

        hardCreateDefaultRecipes(newApplicationUser);

        return new AuthenticationResponse(jwtToken);
    }

    private void checkIfEmailExists(String email) {
        Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findByEmail(email);

        if (applicationUserOptional.isPresent()) {
            throw new EmailAlreadyExistsException(messageSource.getMessage(
                    "exception.authentication.emailAlreadyExists", null, Locale.getDefault()));
        }
    }

    public void sendEmail(ApplicationUser applicationUser, String jwtToken) {
        String verificationLink = "http://localhost:8080/api/v1/auth/verify-email?token=" + jwtToken; // TODO: generalize the link
        String subject =
                messageSource.getMessage("message.authentication.emailSubject", null, Locale.getDefault());
        String text =
                messageSource.getMessage("message.authentication.emailText", null, Locale.getDefault())
                        + "\n\n" + verificationLink;
        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(applicationUser.getEmail());
        email.setSubject(subject);
        email.setText(text);

        javaMailSender.send(email);
    }

    public void verifyEmail(String verificationToken) {
        String email = jwtService.verifyEmail(verificationToken);

        ApplicationUser applicationUser = applicationUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(
                        "exception.authentication.emailNotFound", null, Locale.getDefault())));

        applicationUser.setLocked(false);

        applicationUserRepository.save(applicationUser);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.email(),
                        authenticationRequest.password()
                )
        );

        ApplicationUser applicationUser = applicationUserRepository.findByEmail(authenticationRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(
                        "exception.authentication.emailNotFound", null, Locale.getDefault())));

        String jwtToken = jwtService.generateToken(applicationUser);

        return new AuthenticationResponse(jwtToken);
    }

    private void hardCreateDefaultRecipes(ApplicationUser newApplicationUser) {
        hardCreateDefaultRecipe1(newApplicationUser);
        hardCreateDefaultRecipe2(newApplicationUser);
        hardCreateDefaultRecipe3(newApplicationUser);
    }

    private void hardCreateDefaultRecipe1(ApplicationUser newApplicationUser) {
        TagDTO tagDto1 = tagService.createTag(
                new TagDTO(null, "Chicken", null, null, null),
                newApplicationUser
        );
        TagDTO tagDto2 = tagService.createTag(
                new TagDTO(null, "Dinner", null, null, null),
                newApplicationUser
        );
        TagDTO tagDto3 = tagService.createTag(
                new TagDTO(null, "Meat", null, null, null),
                newApplicationUser
        );
        TagDTO tagDto4 = tagService.createTag(
                new TagDTO(null, "Oven", null, null, null),
                newApplicationUser
        );
        TagDTO tagDto5 = tagService.createTag(
                new TagDTO(null, "Potato", null, null, null),
                newApplicationUser
        );
        TagDTO tagDto6 = tagService.createTag(
                new TagDTO(null, "Quick Recipe", null, null, null),
                newApplicationUser
        );

        RecipeDTO recipeDTO = new RecipeDTO(
                null,
                "Chicken with Potatoes and Lemon in the Oven",
                null,
                null,
                List.of(tagDto1, tagDto2, tagDto3, tagDto4, tagDto5, tagDto6),
                List.of(
                        new IngredientDTO(null, "Whole Chicken", 2000.0, MeasurementUnit.GRAM, null, null),
                        new IngredientDTO(null, "Potato", 5.0, MeasurementUnit.PIECE, null, null),
                        new IngredientDTO(null, "Lemon", 1.0, MeasurementUnit.PIECE, null, null),
                        new IngredientDTO(null, "Garlic", 1.0, MeasurementUnit.PIECE, null, null),
                        new IngredientDTO(null, "Olive Oil", 3.0, MeasurementUnit.TABLESPOON, null, null),
                        new IngredientDTO(null, "Salt", 1.0, MeasurementUnit.TEASPOON, null, null),
                        new IngredientDTO(null, "Pepper", 1.0, MeasurementUnit.TEASPOON, null, null)
                ),
                """
                        Preheat the oven to 200°C.
                        Place the chicken in a roasting pan or baking dish.
                        Arrange the quartered potatoes around the chicken.
                        Drizzle everything with olive oil, ensuring all ingredients are coated.
                        Season with salt, pepper, and your preferred herbs.
                        Place the lemon slices on top of the chicken and potatoes.
                        Transfer the dish to the preheated oven.
                        Roast for approximately 1 to 1.5 hours, or until the chicken is cooked through and the potatoes are golden and tender.
                        Check the internal temperature of the chicken using a meat thermometer; it should read 75°C (165°F) when fully cooked.
                        """,
                null,
                List.of(
                        "https://youtube/jrfgklgepopcsbdknsdfghiuytfdxcvbnmsdfghjjhgf",
                        "https://youtube/jrfgklgepopcsbdknsdfghiuytfdxcvbnmsdfghjjhgfdscvbnbvcxzxkjshgfnjsnsh"
                ),
                null
        );

        RecipeDTO createdRecipeDTO = recipeService.createRecipe(recipeDTO, newApplicationUser);

        byte[] mainPhotoBytes = fileStorageService.getPhoto(
                "src/main/resources/static/images/default-upon-registration/recipe-1-main-photo.png");
        String mainPhotoName = "recipe-1-main-photo.png";
        recipeService.addDefaultRecipeMainPhoto(newApplicationUser.getId(), createdRecipeDTO, mainPhotoBytes, mainPhotoName);

        for (int i = 1; i <= 4; i++) {
            byte[] instructionPhotoBytes1 = fileStorageService.getPhoto(
                    "src/main/resources/static/images/default-upon-registration/recipe-1-instruction-photo-%s.png".formatted(i));
            String instructionPhotoName1 = "recipe-1-instruction-photo-%s.png".formatted(i);
            recipeService.addDefaultRecipeInstructionPhoto(newApplicationUser.getId(), createdRecipeDTO, instructionPhotoBytes1, instructionPhotoName1);
        }
    }

    private void hardCreateDefaultRecipe2(ApplicationUser newApplicationUser) {
        TagDTO tagDto1 = tagService.getTagDTOByName("Dinner", newApplicationUser);
        TagDTO tagDto2 = tagService.getTagDTOByName("Oven", newApplicationUser);
        TagDTO tagDto3 = tagService.getTagDTOByName("Quick Recipe", newApplicationUser);
        TagDTO tagDto4 = tagService.createTag(
                new TagDTO(null, "Spaghetti", null, null, null),
                newApplicationUser
        );
        TagDTO tagDto5 = tagService.createTag(
                new TagDTO(null, "Mom's Recipe", null, null, null),
                newApplicationUser
        );

        RecipeDTO recipeDTO = new RecipeDTO(
                null,
                "Spaghetti with Tomato Sauce Recipe",
                null,
                null,
                List.of(tagDto1, tagDto2, tagDto3, tagDto4, tagDto5),
                List.of(
                        new IngredientDTO(null, "Spaghetti Pasta", 450.0, MeasurementUnit.GRAM, null, null),
                        new IngredientDTO(null, "Crushed Tomatoes", 800.0, MeasurementUnit.GRAM, null, null),
                        new IngredientDTO(null, "Onion", 1.0, MeasurementUnit.PIECE, null, null),
                        new IngredientDTO(null, "Garlic", 1.0, MeasurementUnit.PIECE, null, null),
                        new IngredientDTO(null, "Olive Oil", 60.0, MeasurementUnit.MILLILITER, null, null),
                        new IngredientDTO(null, "Salt", 0.5, MeasurementUnit.TEASPOON, null, null),
                        new IngredientDTO(null, "Pepper", 0.5, MeasurementUnit.TEASPOON, null, null),
                        new IngredientDTO(null, "Fresh Basil ", 1.0, MeasurementUnit.PIECE, null, null),
                        new IngredientDTO(null, "Grated Parmesan Cheese", 1.0, MeasurementUnit.TEASPOON, null, null)
                ),
                """
                        Cook the spaghetti according to the package instructions until al dente. Drain and set aside.
                        Heat the olive oil in a large pan over medium heat.
                        Add the chopped onion and minced garlic to the pan. Sauté until the onion becomes translucent and the garlic is fragrant.
                        Add the canned diced tomatoes, tomato paste, sugar, dried basil, dried oregano, salt, and pepper to the pan. Stir well to combine.
                        Reduce the heat to low and let the sauce simmer for about 15-20 minutes, stirring occasionally to prevent sticking.
                        Taste the sauce and adjust the seasoning if needed.
                        Once the sauce has thickened to your desired consistency, remove it from the heat.
                        Serve the cooked spaghetti onto plates or bowls.
                        Pour the tomato sauce over the spaghetti, ensuring it coats the pasta evenly.
                        If desired, sprinkle grated Parmesan cheese on top.
                        Garnish with fresh basil leaves for an extra touch of flavor and presentation.
                        """,
                null,
                List.of(
                        "https://youtube/jrfgklgepopcsbdknsdfghiuytfdxcvbnmsdfghjjhgf",
                        "https://youtube/jrfgklgepopcsbdknsdfghiuytfdxcvbnmsdfghjjhgfdscvbnbvcxzxkjshgfnjsnsh"
                ),
                null
        );

        RecipeDTO createdRecipeDTO = recipeService.createRecipe(recipeDTO, newApplicationUser);

        byte[] mainPhotoBytes = fileStorageService.getPhoto(
                "src/main/resources/static/images/default-upon-registration/recipe-2-main-photo.png");
        String mainPhotoName = "recipe-2-main-photo.png";
        recipeService.addDefaultRecipeMainPhoto(newApplicationUser.getId(), createdRecipeDTO, mainPhotoBytes, mainPhotoName);

        for (int i = 1; i <= 3; i++) {
            byte[] instructionPhotoBytes1 = fileStorageService.getPhoto(
                    "src/main/resources/static/images/default-upon-registration/recipe-2-instruction-photo-%s.png".formatted(i));
            String instructionPhotoName1 = "recipe-2-instruction-photo-%s.png".formatted(i);
            recipeService.addDefaultRecipeInstructionPhoto(newApplicationUser.getId(), createdRecipeDTO, instructionPhotoBytes1, instructionPhotoName1);
        }

    }

    private void hardCreateDefaultRecipe3(ApplicationUser newApplicationUser) {
        RecipeDTO recipeDTO = new RecipeDTO(
                null,
                "Autumn Pie Made From Tart Apples",
                null,
                null,
                null,
                List.of(
                        new IngredientDTO(null, "Refrigerated Pie Crust", 2000.0, MeasurementUnit.GRAM, null, null),
                        new IngredientDTO(null, "Thinly Sliced Apples", 5.0, MeasurementUnit.PIECE, null, null),
                        new IngredientDTO(null, "Granulated Sugar", 1.0, MeasurementUnit.PIECE, null, null),
                        new IngredientDTO(null, "Flour", 1.0, MeasurementUnit.PIECE, null, null),
                        new IngredientDTO(null, "Ground Cinnamon", 3.0, MeasurementUnit.TABLESPOON, null, null),
                        new IngredientDTO(null, "Ground Nutmeg", 1.0, MeasurementUnit.TEASPOON, null, null),
                        new IngredientDTO(null, "Salt", 1.0, MeasurementUnit.TEASPOON, null, null),
                        new IngredientDTO(null, "Unsalted Butter", 1.0, MeasurementUnit.TEASPOON, null, null)
                ),
                """
                        In a large bowl, toss the sliced tart apples with lemon juice to prevent browning.
                        In a bowl, combine the granulated sugar, brown sugar, all-purpose flour, ground cinnamon, ground nutmeg, and salt. Mix well.
                        Add the sugar and spice mixture to the apples, tossing until the apples are evenly coated.
                        Place one sheet of ready-made pie crust in a 9-inch pie dish, pressing it gently against the bottom and sides.
                        Pour the apple mixture into the pie crust, spreading it out evenly. Dot the top of the apples with small pieces of unsalted butter.
                        Place the second sheet of ready-made pie crust over the apple filling. You can create a lattice design or simply cover the pie and cut a few slits in the top to allow steam to escape.
                        Trim the excess dough from the edges and crimp the crust to seal it.
                        Brush the top crust with the beaten egg to give it a shiny finish.
                        Sprinkle some additional sugar and cinnamon over the top crust.
                        Place the pie on a baking sheet to catch any drips and bake in the preheated oven for about 45-50 minutes.
                        """,
                null,
                List.of(
                        "https://youtube/jrfgklgepopcsbdknsdfghiuytfdxcvbnmsdfghjjhgf",
                        "https://youtube/jrfgklgepopcsbdknsdfghiuytfdxcvbnmsdfghjjhgfdscvbnbvcxzxkjshgfnjsnsh"
                ),
                null
        );

        recipeService.createRecipe(recipeDTO, newApplicationUser);
    }
}
