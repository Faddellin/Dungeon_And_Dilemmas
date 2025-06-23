package org.DAD.application.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.DAD.application.model.CommonModels.ResponseModel;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String message = "";
        if (request.getAttribute("expired") != null) {
            message += "Token has expired";
        }
        else if (request.getAttribute("invalid") != null) {
            message += "Invalid token";
        }
        else {
            message += "Full authentication is required to access this resource";
        }

        ResponseModel errorResponse = new ResponseModel();
        errorResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
        Dictionary<String, String> errors = new Hashtable<>();
        errors.put("Auth", message);
        errorResponse.setErrors(errors);

        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
} 