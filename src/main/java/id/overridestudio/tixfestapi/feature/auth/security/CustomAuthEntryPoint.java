package id.overridestudio.tixfestapi.feature.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.overridestudio.tixfestapi.core.dto.response.WebResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        WebResponse<?> webResponse = new WebResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                authException.getMessage(),
                null,
                null
        );
        String responseString = objectMapper.writeValueAsString(webResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(responseString);
    }
}
