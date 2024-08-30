package com.boilerplate.welli.Created.By.Welli.Ardiansyah.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Log detailed error information
        logger.error("Unauthorized error: URI: {}, Message: {}", request.getRequestURI(), authException.getMessage());

        // Set the response content type and status
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Prepare dynamic error response
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", getErrorMessage(authException));
        body.put("path", request.getServletPath());

        // Write response to output stream
        try {
            mapper.writeValue(response.getOutputStream(), body);
        } catch (IOException e) {
            logger.error("Exception occurred while writing to response: {}", e.getMessage());
            throw new ServletException("Failed to write JSON response", e);
        }
    }

    private String getErrorMessage(AuthenticationException authException) {
        if (authException.getCause() != null) {
            return authException.getCause().getMessage();
        }
        return authException.getMessage();
    }
}
