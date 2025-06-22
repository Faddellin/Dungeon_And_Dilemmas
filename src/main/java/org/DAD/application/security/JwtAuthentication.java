package org.DAD.application.security;

import lombok.Getter;
import org.DAD.domain.entity.User.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
public class JwtAuthentication implements Authentication {
    private boolean authenticated;
    private final UUID id;
    private final String username;
    private final UserRole userRole;

    public JwtAuthentication(UUID id, String username, UserRole userRole) {
        this.id = id;
        this.username = username;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userRole != null) {
            return Collections.singleton(() -> "ROLE_" + userRole.name());
        }
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.id;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
