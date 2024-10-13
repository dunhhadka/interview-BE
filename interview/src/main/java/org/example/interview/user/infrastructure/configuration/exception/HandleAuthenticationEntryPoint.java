package org.example.interview.user.infrastructure.configuration.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.interview.user.application.utils.JsonUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class HandleAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        log.error("check");
        if (request.getAttribute("closed_session") != null) {
            response.setStatus(401);
            var message = ErrorMessage.builder()
                    .message("Have an account on another device logged in")
                    .build();
            response.setContentType("application/json");
            response.getWriter().write(JsonUtils.marshal(message));
        }
    }
}
