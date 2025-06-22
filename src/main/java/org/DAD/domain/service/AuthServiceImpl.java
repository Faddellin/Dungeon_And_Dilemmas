package org.DAD.domain.service;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.User.TokenResponseModel;
import org.DAD.application.model.User.UserLoginModel;
import org.DAD.application.model.User.UserRegisterModel;
import org.DAD.application.service.LoggedOutTokenService;
import org.DAD.application.security.JwtProvider;
import org.DAD.application.service.AuthService;
import org.DAD.application.service.UserService;
import org.DAD.domain.entity.User.User;
import org.DAD.domain.entity.User.UserRole;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final LoggedOutTokenService loggedOutTokenService;

    @Transactional
    public TokenResponseModel register(UserRegisterModel userRegisterModel) throws ExceptionWrapper {
        if (userService.existsByEmail(userRegisterModel.getEmail())) {
            ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
            badRequestException.addError("Email", "User with this email already exists");
            throw badRequestException;
        }
        User user = new User();
        user.setUserName(userRegisterModel.getUserName());
        user.setEmail(userRegisterModel.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userRegisterModel.getPassword()));
        user.setRole(UserRole.Player);

        userService.save(user);

        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiryDate(jwtProvider.getRefreshTokenExpirationDate());
        userService.save(user);

        return new TokenResponseModel(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponseModel login(UserLoginModel userLoginModel) throws ExceptionWrapper {
        final User user = userService.findByEmail(userLoginModel.getEmail());
        ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
        badRequestException.addError("Credentials", "Incorrect password or email");

        if (user == null) {
            throw badRequestException;
        }

        if (passwordEncoder.matches(userLoginModel.getPassword(), user.getPasswordHash())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);

            user.setRefreshToken(refreshToken);
            user.setRefreshTokenExpiryDate(jwtProvider.getRefreshTokenExpirationDate());
            userService.save(user);
            return new TokenResponseModel(accessToken, refreshToken);
        }
        else {
            throw badRequestException;
        }
    }

    public TokenResponseModel getAccessToken(String refreshToken) throws ExceptionWrapper {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            ExceptionWrapper entityNotFoundEx = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundEx.addError("Not found", "User not found");

            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final UUID id = UUID.fromString(claims.get("user_id", String.class));
            final Optional<User> userOpt = userService.findById(id);
            if (userOpt.isEmpty()) {
                throw entityNotFoundEx;
            }
            final User user = userOpt.get();

            String savedRefreshToken = user.getRefreshToken();
            Date savedExpiryDate = user.getRefreshTokenExpiryDate();

            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken) && savedExpiryDate.after(new Date())) {
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new TokenResponseModel(accessToken, null);
            }
        }
        ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
        badRequestException.addError("Refresh Token", "Invalid or expired refresh token");
        throw badRequestException;
    }

    @Transactional
    public TokenResponseModel refresh(String refreshToken) throws ExceptionWrapper {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
            badRequestException.addError("Refresh Token", "Invalid refresh token");
            throw badRequestException;
        }
        ExceptionWrapper entityNotFoundEx = new ExceptionWrapper(new EntityNotFoundException());
        entityNotFoundEx.addError("Not found", "User not found");
        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final UUID id = UUID.fromString(claims.get("user_id", String.class));
        final Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            throw entityNotFoundEx;
        }
        final User user = userOpt.get();

        String savedRefreshToken = user.getRefreshToken();
        Date savedExpiryDate = user.getRefreshTokenExpiryDate();

        if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken) && savedExpiryDate.after(new Date())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String newRefreshToken = jwtProvider.generateRefreshToken(user);

            user.setRefreshToken(newRefreshToken);
            user.setRefreshTokenExpiryDate(jwtProvider.getRefreshTokenExpirationDate());
            userService.save(user);
            return new TokenResponseModel(accessToken, newRefreshToken);
        }

        ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
        badRequestException.addError("Refresh Token", "Invalid or expired refresh token");
        throw badRequestException;
    }

    @Transactional
    public void logout(UUID userId) throws ExceptionWrapper {
        ExceptionWrapper entityNotFoundEx = new ExceptionWrapper(new EntityNotFoundException());
        entityNotFoundEx.addError("Not found", "User not found");
        final Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            throw entityNotFoundEx;
        }
        final User user = userOpt.get();
        
        // Получаем информацию о запросе
        HttpServletRequest request = getCurrentRequest();
        String ipAddress = getClientIpAddress(request);
        String userAgent = request != null ? request.getHeader("User-Agent") : "Unknown";
        
        // Сохраняем refresh-токен в память как залогаученный
        if (user.getRefreshToken() != null) {
            loggedOutTokenService.addLoggedOutToken(
                user.getRefreshToken(), 
                userId, 
                ipAddress, 
                userAgent
            );
        }
        
        // Очищаем refresh-токен и дату истечения
        user.setRefreshToken(null);
        user.setRefreshTokenExpiryDate(null);
        userService.save(user);
    }
    
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        if (request == null) return "Unknown";
        
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
