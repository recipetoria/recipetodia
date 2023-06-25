package com.jit.rec.recipetoria.applicationUser.settings;

import com.jit.rec.recipetoria.applicationUser.ApplicationUserDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User Settings")
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
public interface ApplicationUserSettingsApi {

    @Operation(
            summary = "Get User personal information",
            description = "Retrieves User personal information"
    )
    @ApiResponse(
            responseCode = "200", description = "User personal information retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @GetMapping
    ResponseEntity<Response> showSettings();

    @Operation(
            summary = "Update User name",
            description = "Updates User name"
    )
    @ApiResponse(
            responseCode = "200", description = "User personal information updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "applicationUserInfo",
                    required = true,
                    description = """
                            New User personal information \n
                            email: ignored \n
                            name: required \n
                            profilePhoto: ignored \n
                            password: ignored
                            """
            )
    })
    @PutMapping("/personal-info")
    ResponseEntity<Response> updateApplicationUserInfo(ApplicationUserDTO applicationUserInfo);

    @Operation(
            summary = "Get User profile photo",
            description = "Retrieves User profile photo"
    )
    @ApiResponse(
            responseCode = "200", description = "User profile photo retrieved successfully",
            content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)
    )
    @GetMapping
    ResponseEntity<Response> getApplicationUserPhoto();

    @Operation(
            summary = "Update User profile photo",
            description = "Updates User profile photo"
    )
    @ApiResponse(
            responseCode = "200", description = "User profile photo updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "file",
                    required = true,
                    description = "New profile photo"
            )
    })
    @PutMapping("/photo")
    ResponseEntity<Response> updateApplicationUserPhoto(MultipartFile file);

    @Operation(
            summary = "Delete User profile photo",
            description = "Deletes User profile photo"
    )
    @ApiResponse(
            responseCode = "204", description = "User profile photo deleted successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @DeleteMapping("/photo-delete")
    ResponseEntity<Response> deleteApplicationUserPhoto();

    @Operation(
            summary = "Check User password",
            description = "Checks whether User password matches"
    )
    @ApiResponse(
            responseCode = "200", description = "User password checked successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "applicationUserInfo",
                    required = true,
                    description = """
                            New User personal information \n
                            email: ignored \n
                            name: ignored \n
                            profilePhoto: ignored \n
                            password: required
                            """
            )
    })
    @PostMapping
    ResponseEntity<Response> checkApplicationUserPassword(ApplicationUserDTO applicationUserInfo);

    @Operation(
            summary = "Update User password",
            description = "Updates User password"
    )
    @ApiResponse(
            responseCode = "200", description = "User password updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @Parameters({
            @Parameter(
                    name = "applicationUserInfo",
                    required = true,
                    description = """
                            New User personal information \n
                            email: ignored \n
                            name: ignored \n
                            profilePhoto: ignored \n
                            password: required
                            """
            )
    })
    @PutMapping("/password")
    ResponseEntity<Response> updateApplicationUserPassword(ApplicationUserDTO applicationUserInfo);

    @Operation(
            summary = "Delete User account",
            description = "Deletes User account"
    )
    @ApiResponse(
            responseCode = "204", description = "User account deleted successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @DeleteMapping("/account")
    ResponseEntity<Response> deleteApplicationUser();
}
