package org.DAD.application.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.DAD.application.service.LoggedOutTokenService;
import org.DAD.domain.entity.User.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final long accessLifetimeMinutes;
    private final long refreshLifetimeDays;
    private final LoggedOutTokenService loggedOutTokenService;

    public JwtProvider(@Value("${jwt.secret.access}") String jwtAccessSecret,
                       @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
                       @Value("${jwt.lifetime.access-minutes}") long accessLifetimeMinutes,
                       @Value("${jwt.lifetime.refresh-days}") long refreshLifetimeDays,
                       LoggedOutTokenService loggedOutTokenService) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.accessLifetimeMinutes = accessLifetimeMinutes;
        this.refreshLifetimeDays = refreshLifetimeDays;
        this.loggedOutTokenService = loggedOutTokenService;
    }

    public String generateAccessToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now
                .plusMinutes(accessLifetimeMinutes)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        final Date accessExpirationDate = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .subject(user.getUserName())
                .expiration(accessExpirationDate)
                .signWith(jwtAccessSecret)
                .claim("user_id", user.getId().toString())
                .claim("role", user.getRole())
                .compact();
    }

    public String generateRefreshToken(User user) {
        final Date refreshExpirationDate = getRefreshTokenExpirationDate();
        return Jwts.builder()
                .subject(user.getUserName())
                .expiration(refreshExpirationDate)
                .signWith(jwtRefreshSecret)
                .claim("user_id", user.getId().toString())
                .claim("role", user.getRole())
                .compact();
    }

    public Date getRefreshTokenExpirationDate() {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now
                .plusDays(refreshLifetimeDays)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        return Date.from(refreshExpirationInstant);
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, jwtAccessSecret) && !isTokenLoggedOut(token);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, jwtRefreshSecret) && !isTokenLoggedOut(token);
    }

    private boolean validateToken(String token, SecretKey secret) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported jwt", e);
        } catch (MalformedJwtException e) {
            log.error("Malformed jwt", e);
        } catch (SignatureException e) {
            log.error("Invalid signature", e);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    private boolean isTokenLoggedOut(String token) {
        return loggedOutTokenService.isTokenLoggedOut(token);
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(String token, SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
