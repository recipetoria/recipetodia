package com.jit.rec.recipetoria.security.auth;

import com.jit.rec.recipetoria.exception.EmailAlreadyExistsException;
import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserRepository;
import com.jit.rec.recipetoria.applicationUser.ApplicationUserRole;
import com.jit.rec.recipetoria.ingredient.IngredientDTO;
import com.jit.rec.recipetoria.ingredient.MeasurementUnit;
import com.jit.rec.recipetoria.recipe.RecipeDTO;
import com.jit.rec.recipetoria.recipe.RecipeService;
import com.jit.rec.recipetoria.security.jwt.JwtService;
import com.jit.rec.recipetoria.tag.TagDTO;
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

//        createDefaultRecipes(newApplicationUser);

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

    private void createDefaultRecipes(ApplicationUser newApplicationUser) {
        RecipeDTO recipeDTO = new RecipeDTO(
                null,
                "Chicken with potatoes and lemon in the oven",
                null,
                null,
                List.of(
                        new TagDTO(null, "Chicken", null, null, null),
                        new TagDTO(null, "Dinner", null, null, null),
                        new TagDTO(null, "Meat", null, null, null),
                        new TagDTO(null, "Oven", null, null, null),
                        new TagDTO(null, "Potato", null, null, null),
                        new TagDTO(null, "Quick Recipe", null, null, null)
                ),
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
                        "https://youtube/jrfgklgepopcsbdknsdfghiuytfdxcvbnmsdfghjjhgf",
                        "https://youtube/jrfgklgepopcsbdknsdfghiuytfdxcvbnmsdfghjjhgfdscvbnbvcxzxkjshgfnjsnsh"
                ),
                null
        );

        recipeService.createRecipe(recipeDTO, newApplicationUser);
    }
}
