package com.tradeflow.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * CORS Configuration for TradeFlow.
 *
 * IMPORTANT — CORS NOTE FOR GITHUB PAGES + LOCALHOST:
 * ────────────────────────────────────────────────────
 * Your frontend is on: https://vansh-jyotishi.github.io (HTTPS)
 * Your backend is on:  http://localhost:8080 (HTTP)
 *
 * Browsers block HTTPS → HTTP requests ("mixed content").
 * You have two options to resolve this:
 *
 * OPTION 1 (Recommended for development):
 *   Open the frontend from localhost too (e.g., Live Server on port 5500)
 *   instead of from GitHub Pages. Both will be HTTP → works fine.
 *
 * OPTION 2 (Production-like):
 *   Run your backend with HTTPS using a self-signed certificate:
 *     - Generate: keytool -genkeypair -alias tradeflow -keyalg RSA -keysize 2048
 *                 -storetype PKCS12 -keystore keystore.p12 -validity 365
 *     - Add to application.properties:
 *         server.ssl.key-store=classpath:keystore.p12
 *         server.ssl.key-store-password=your_password
 *         server.ssl.key-store-type=PKCS12
 *     - Then backend runs on https://localhost:8080
 *
 * OPTION 3 (Quick test):
 *   Use a tunneling service like ngrok to expose localhost with HTTPS:
 *     ngrok http 8080
 *   Then update BASE_URL in api.js with the ngrok HTTPS URL.
 */
@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:https://vansh-jyotishi.github.io,http://localhost:3000,http://localhost:5500,http://127.0.0.1:5500}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Parse comma-separated origins from config
        config.setAllowedOrigins(List.of(allowedOrigins.split(",")));

        config.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
