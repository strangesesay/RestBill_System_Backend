package com.cohort5.RestBil_System_Backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS (Cross-Origin Resource Sharing) configuration for the RestBill System.
 * Configures allowed origins, methods, headers, and credentials support based on environment.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed.origins:http://localhost:4200}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Configure allowed origins based on environment
                .allowedOrigins(allowedOrigins)
                // Configure allowed HTTP methods
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                // Configure allowed headers
                .allowedHeaders("Authorization", "Content-Type", "Accept")
                // Enable credentials support (cookies, authorization headers)
                .allowCredentials(true)
                // Set preflight cache max age (in seconds) - 1 hour
                .maxAge(3600);
    }
}
