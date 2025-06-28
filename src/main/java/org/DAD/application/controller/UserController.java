package org.DAD.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.CommonModels.ResponseModel;
import org.DAD.application.model.User.*;
import org.DAD.application.security.JwtAuthentication;
import org.DAD.application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get my profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Profile was successfully retrieved",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserModel.class)
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )})
    })
    public ResponseEntity<UserModel> getMyProfile() throws ExceptionWrapper {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication jwtAuth) {
            UUID userId = jwtAuth.getId();
            return new ResponseEntity<>(userService.getProfileById(userId), HttpStatus.OK);
        }
        else {
            ExceptionWrapper authEx = new ExceptionWrapper(new AuthException());
            authEx.addError("Authentication", "Invalid authentication");
            throw authEx;
        }
    }

    @GetMapping("/profile/{userId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get other user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Profile was successfully retrieved",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserOtherModel.class)
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )})
    })
    public ResponseEntity<UserOtherModel> getUsersProfile(@PathVariable UUID userId) throws ExceptionWrapper {
        var userModel =  userService.getProfileById(userId);
        return new ResponseEntity<>(new UserOtherModel(
                userModel.id, userModel.userName,
                userModel.totalPoints, userModel.bestUsersGame), HttpStatus.OK);
    }

    @GetMapping("/list")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get users list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Users list was successfully retrieved",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserListModel.class)
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )})
    })
    public ResponseEntity<UserListModel> getAllUsers(@RequestParam(required = false) String name) {
        UserListModel users = userService.findUsers(name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/profile/edit")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Edit profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Profile was successfully edited",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserModel.class)
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )})
    })
    public UserModel editProfile(@Valid @RequestBody UserEditModel model) throws ExceptionWrapper {
        UUID userId = getCurrentUserId();
        return userService.editProfile(userId, model);
    }

    @PutMapping("/profile/edit-password")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Edit password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Password was successfully edited"),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )})
    })
    public void editPassword(@Valid @RequestBody UserEditPasswordModel model) throws ExceptionWrapper {
        UUID userId = getCurrentUserId();
        userService.editPassword(userId, model);
    }

    private UUID getCurrentUserId() throws ExceptionWrapper {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication jwtAuth) {
            return jwtAuth.getId();
        } else {
            ExceptionWrapper authEx = new ExceptionWrapper(new AuthException());
            authEx.addError("Authentication", "Invalid authentication");
            throw authEx;
        }
    }
}
