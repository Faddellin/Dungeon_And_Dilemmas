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
import lombok.extern.slf4j.Slf4j;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.CommonModels.ResponseModel;
import org.DAD.application.model.User.RefreshTokenRequestModel;
import org.DAD.application.model.User.TokenResponseModel;
import org.DAD.application.model.User.UserLoginModel;
import org.DAD.application.model.User.UserRegisterModel;
import org.DAD.application.security.JwtAuthentication;
import org.DAD.application.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User successfully registered",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponseModel.class)
            )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )})
    })
    public ResponseEntity<TokenResponseModel> register(@Valid @RequestBody UserRegisterModel userRegisterModel) throws ExceptionWrapper {
        return ResponseEntity.ok(authService.register(userRegisterModel));
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User successfully logged in",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponseModel.class)
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )})
    })
    public ResponseEntity<TokenResponseModel> login(@Valid @RequestBody UserLoginModel userLoginModel) throws ExceptionWrapper {
        return ResponseEntity.ok(authService.login(userLoginModel));
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Logout")
    public void logout() throws ExceptionWrapper {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication jwtAuth) {
            UUID userId = jwtAuth.getId();
            authService.logout(userId);
        }
        else {
            ExceptionWrapper authException = new ExceptionWrapper(new AuthException());
            authException.addError("Auth", "Authentication Failed");
            throw authException;
        }
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User successfully registered",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponseModel.class)
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )})
    })
    public ResponseEntity<TokenResponseModel> refreshToken(@Valid @RequestBody RefreshTokenRequestModel model) throws ExceptionWrapper {
        final TokenResponseModel tokenResponseModel = authService.refresh(model.refreshToken);
        return new ResponseEntity<>(tokenResponseModel, HttpStatus.OK);
    }
}
