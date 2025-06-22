package org.DAD.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.DAD.application.model.CommonModels.ResponseModel;
import org.DAD.application.model.User.*;
import org.DAD.application.security.JwtAuthentication;
import org.DAD.application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<UserModel> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication) {
            JwtAuthentication jwtAuth = (JwtAuthentication) authentication;
            UUID userId = jwtAuth.getId();
            return new ResponseEntity<>(userService.getProfileById(userId), HttpStatus.OK);
        } else {
            throw new RuntimeException("Invalid authentication");
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
    public ResponseEntity<UserOtherModel> getUsersProfile(@PathVariable UUID userId) {
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
    public ResponseEntity<UserListModel> getAllUsers() {
        UserListModel users = userService.getAllUsersExceptAdmins();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/profile/edit")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Редактировать профиль", description = "Редактирует профиль текущего пользователя")
    public void editProfile(@RequestBody UserEditModel model) {

    }

    @PutMapping("/profile/edit-password")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Изменить пароль", description = "Изменяет пароль текущего пользователя")
    public void editPassword(@RequestBody UserEditPasswordModel model) {

    }
}
