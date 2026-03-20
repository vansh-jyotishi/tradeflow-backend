package com.tradeflow.backend.config;

import com.tradeflow.backend.security.JwtAuthenticationEntryPoint;
import com.tradeflow.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authEntryPoint;
    private final JwtAuthenticationFilter jwtFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/carriers/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/shipments/track/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/analytics/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/site-settings/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/banners/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/partners/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/trade-routes/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/globe-markers/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/platform-stats/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/inquiries").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/bookings/estimate").permitAll()
                // Admin endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // All other endpoints require authentication
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
