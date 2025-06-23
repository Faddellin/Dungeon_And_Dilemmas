package org.DAD.application.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.DAD.application.security.JwtAuthentication;
import org.DAD.application.security.JwtProvider;
import org.DAD.application.security.JwtUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        try {
            if (token != null) {
                if (jwtProvider.validateAccessToken(token)) {
                    final Claims claims = jwtProvider.getAccessClaims(token);
                    final JwtAuthentication authentication = JwtUtils.generate(claims);
                    authentication.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    request.setAttribute("invalid", true);
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
            request.setAttribute("expired", true);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            log.error("Authentication error", e);
            request.setAttribute("invalid", true);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
