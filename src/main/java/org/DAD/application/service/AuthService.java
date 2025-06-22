package org.DAD.application.service;

import jakarta.security.auth.message.AuthException;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.User.TokenResponseModel;
import org.DAD.application.model.User.UserLoginModel;
import org.DAD.application.model.User.UserRegisterModel;
import java.util.UUID;

public interface AuthService {
    TokenResponseModel login(UserLoginModel userLoginModel) throws ExceptionWrapper;
    TokenResponseModel getAccessToken(String refreshToken) throws ExceptionWrapper;
    TokenResponseModel refresh(String refreshToken) throws ExceptionWrapper;
    TokenResponseModel register(UserRegisterModel userRegisterModel) throws ExceptionWrapper;
    void logout(UUID userId) throws ExceptionWrapper;
}
