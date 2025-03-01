package id.overridestudio.tixfestapi.feature.auth.security;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthEntryPoint customAuthEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .requiresChannel(channel ->
//                        channel.anyRequest().requiresSecure())
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(config -> {
                    config.accessDeniedHandler(customAccessDeniedHandler);
                    config.authenticationEntryPoint(customAuthEntryPoint);
                })
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req ->
                                req.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh-token").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/forgot-password/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/v1/customers/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/v1/event-organizers/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/api/images/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://electric-merely-gorilla.ngrok-free.app", "https://cors-test.codehappy.dev", "https://electric-merely-gorilla.ngrok-free.app/swagger-ui/index.html/"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
