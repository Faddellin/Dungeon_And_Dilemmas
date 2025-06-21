package org.DAD.application.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.DAD.application.model.User.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {


    @PostMapping("register")
    public TokenResponseModel register(@RequestBody UserRegisterModel model) {
        return null;
    }

    @PostMapping("login")
    public TokenResponseModel login(@RequestBody UserLoginModel model) {
        return null;
    }

    @PostMapping("logout")
    @SecurityRequirement(name = "JWT")
    public void logout() {

    }

    @PostMapping("refresh-token")
    public TokenResponseModel refreshToken(@RequestBody RefreshTokenRequestModel model) {
        return null;
    }

    @GetMapping("profile")
    @SecurityRequirement(name = "JWT")
    public UserModel getMyProfile() {
        return null;
    }

    @GetMapping("profile/{userId}")
    @SecurityRequirement(name = "JWT")
    public UserModel getUsersProfile(@PathVariable UUID userId) {
        return null;
    }

    @PutMapping("profile/edit")
    @SecurityRequirement(name = "JWT")
    public void editProfile(@RequestBody UserEditModel model) {

    }

    @PutMapping("profile/edit-password")
    @SecurityRequirement(name = "JWT")
    public void editPassword(@RequestBody UserEditPasswordModel model) {

    }
}
