package com.jit.rec.recipetoria.service;

import com.github.javafaker.Faker;
import com.github.javafaker.File;
import com.jit.rec.recipetoria.dto.ApplicationUserDTO;
import com.jit.rec.recipetoria.entity.ApplicationUser;
import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.entity.Tag;
import com.jit.rec.recipetoria.repository.ApplicationUserRepository;
import com.jit.rec.recipetoria.security.applicationUser.ApplicationUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUserSettingsServiceTest {

    private ApplicationUserSettingsService underTest;
    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MessageSource messageSource;
    @Mock
    private Authentication authentication;

    private final Faker FAKER = new Faker();
    private final Random RANDOM = new Random();
    private static final String RESOURCES_DIRECTORY = "src/main/resources/";
    private static final String PROFILE_PHOTO_DIRECTORY = "static/images/user-%d/profile-photo/";
    private static final String PROFILE_PHOTO_NAME = "%d-profile-photo";

    @BeforeEach
    void setUp() {
        underTest = new ApplicationUserSettingsService(applicationUserRepository, passwordEncoder, messageSource);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testGetApplicationUser() {
        // Given
        String applicationUserEmail = FAKER.internet().emailAddress();
        String applicationUserName = FAKER.name().fullName();
        String applicationUserPhoto = FAKER.file().fileName(null, null, "jpg", null);

        ApplicationUser applicationUser = ApplicationUser.builder()
                .email(applicationUserEmail)
                .name(applicationUserName)
                .photo(applicationUserPhoto)
                .build();

        when(authentication.getPrincipal()).thenReturn(applicationUser);

        // When
        ApplicationUserDTO userDTO = underTest.getApplicationUser();

        // Then
        assertThat(userDTO.email()).isEqualTo(applicationUserEmail);
        assertThat(userDTO.name()).isEqualTo(applicationUserName);
        assertThat(userDTO.photo()).isEqualTo(applicationUserPhoto);
        assertThat(userDTO.password()).isEqualTo(null);
    }

    @Test
    void testCheckIfExistsTrue() {
        // Given
        String email = FAKER.internet().emailAddress();
        when(applicationUserRepository.findByEmail(email)).thenReturn(Optional.of(new ApplicationUser()));

        // When
        boolean actual = underTest.checkIfExists(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void testCheckIfExistsFalse() {
        // Given
        String email = FAKER.internet().emailAddress();
        when(applicationUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        boolean actual = underTest.checkIfExists(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void testUpdatePersonalInfo() {
        // Given
        String applicationUserInfoEmail = FAKER.internet().emailAddress();
        String applicationUserInfoName = FAKER.name().fullName();;
        String applicationUserInfoPhoto = FAKER.file().fileName(null, null, "jpg", null);

        ApplicationUserDTO applicationUserInfo = new ApplicationUserDTO(
                applicationUserInfoEmail,
                applicationUserInfoName,
                applicationUserInfoPhoto,
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

        ApplicationUser updatedApplicationUser = new ApplicationUser(
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
        if (applicationUserInfoName != null) {
            updatedApplicationUser.setName(applicationUserInfoName);
        }

        when(authentication.getPrincipal()).thenReturn(applicationUser);
        when(applicationUserRepository.save(applicationUser)).thenReturn(updatedApplicationUser);

        // When
        ApplicationUserDTO actual = underTest.updatePersonalInfo(applicationUserInfo);

        // Then
        assertThat(actual.email()).isEqualTo(applicationUserEmail);
        if (applicationUserInfoName != null) {
            assertThat(actual.name()).isEqualTo(applicationUserInfoName);
        } else {
            assertThat(actual.name()).isEqualTo(applicationUserName);
        }
        assertThat(actual.photo()).isEqualTo(applicationUserPhoto);
        assertThat(actual.password()).isEqualTo(null);

        assertThat(updatedApplicationUser).isEqualTo(applicationUser);
        assertThat(updatedApplicationUser.getId()).isEqualTo(applicationUserId);
        assertThat(updatedApplicationUser.getApplicationUserRole()).isEqualTo(applicationUserRole);
        assertThat(updatedApplicationUser.getEmail()).isEqualTo(applicationUserEmail);
        assertThat(updatedApplicationUser.getPassword()).isEqualTo(applicationUserPassword);
        if (applicationUserInfoName != null) {
            assertThat(updatedApplicationUser.getName()).isEqualTo(applicationUserInfoName);
        } else {
            assertThat(updatedApplicationUser.getName()).isEqualTo(applicationUserName);
        }
        assertThat(updatedApplicationUser.getPhoto()).isEqualTo(applicationUserPhoto);
        assertThat(updatedApplicationUser.isLocked()).isEqualTo(applicationUserLocked);
        assertThat(updatedApplicationUser.getShoppingList()).isEqualTo(applicationUserShoppingList);
        assertThat(updatedApplicationUser.getListOfRecipes()).isEqualTo(applicationUserRecipes);
        assertThat(updatedApplicationUser.getTags()).isEqualTo(applicationUserTags);
    }

    @Test
    void testUpdateProfilePhoto_File() throws IOException {
        // Given
        when(messageSource.getMessage("profilePhoto.supportedExtensions", null, Locale.getDefault()))
                .thenReturn(FAKER.letterify(".jpg,.jpeg,.png,.gif"));

        Long applicationUserId = RANDOM.nextLong(1, 1000);
        String applicationUserEmail = FAKER.internet().emailAddress();
        String applicationUserName = FAKER.name().fullName();
        String applicationUserPhoto = String.format(PROFILE_PHOTO_DIRECTORY, applicationUserId) +
                String.format(PROFILE_PHOTO_NAME, applicationUserId) + ".png";

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(applicationUserId);
        applicationUser.setEmail(applicationUserEmail);
        applicationUser.setName(applicationUserName);
        applicationUser.setPhoto(applicationUserPhoto);

        when(authentication.getPrincipal()).thenReturn(applicationUser);

        Path filePath = Paths.get(RESOURCES_DIRECTORY + applicationUserPhoto);
        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);

        String fileOriginalFileName = "test.jpg";
        MockMultipartFile file = new MockMultipartFile("file", fileOriginalFileName, "image/jpeg", "test-image".getBytes());
        String newFileName = String.format(PROFILE_PHOTO_DIRECTORY, applicationUserId) +
                String.format(PROFILE_PHOTO_NAME, applicationUserId) +
                ".jpg";

        ApplicationUserDTO applicationUserDTO = new ApplicationUserDTO(
                applicationUserEmail,
                applicationUserName,
                newFileName,
                null
        );

        // When
        ApplicationUserDTO actual = underTest.updateProfilePhoto(file);

        // Then
        assertThat(applicationUserDTO).isEqualTo(actual);
        verify(file, times(1)).getOriginalFilename();
    }
}