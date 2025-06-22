package org.DAD.application.security;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.DAD.domain.entity.User.UserRole;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final String userId = claims.get("user_id", String.class);
        final String roleStr = claims.get("role", String.class);
        final UserRole userRole = UserRole.valueOf(roleStr);
        final String username = claims.getSubject();

        return new JwtAuthentication(UUID.fromString(userId), username, userRole);
    }

}
