package id.overridestudio.tixfestapi.feature.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.overridestudio.tixfestapi.core.dto.response.WebResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        WebResponse<?> webResponse = new WebResponse<>(
                HttpStatus.FORBIDDEN.value(),
                accessDeniedException.getMessage(),
                null,
                null
        );
        String responseString = objectMapper.writeValueAsString(webResponse);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(responseString);
    }
}
