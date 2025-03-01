package id.overridestudio.tixfestapi.feature.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.overridestudio.tixfestapi.core.dto.response.WebResponse;
import id.overridestudio.tixfestapi.feature.auth.service.JwtService;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.UserAccount;
import id.overridestudio.tixfestapi.feature.usermanagement.service.UserAccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserAccountService userAccountService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = jwtService.extractTokenFromRequest(request);
            if (token != null) {

                if (jwtService.isTokenBlacklisted(token)) {
                    WebResponse<?> webResponse = new WebResponse<>(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Unauthorized",
                            null,
                            null
                    );
                    String stringJson = objectMapper.writeValueAsString(webResponse);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write(stringJson);
                    return;
                }

                String userId = jwtService.getUserId(token);
                UserAccount userAccount = userAccountService.getOne(userId);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userAccount,
                        null,
                        userAccount.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
