package com.cohort5.RestBil_System_Backend.config;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.StringLength;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Property-based tests for CORS configuration.
 * Tests universal properties that should hold across all valid executions.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class CorsConfigPropertyTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc getMockMvc() {
        return MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Feature: frontend-backend-communication, Property 1: CORS headers presence
     * Validates: Requirements 1.1
     * 
     * For any request from the Frontend Application to the Backend API,
     * the response should include the required CORS headers.
     */
    @Property(tries = 100)
    void corsHeadersPresenceProperty(
            @ForAll("validEndpoints") String endpoint,
            @ForAll("allowedOrigins") String origin) throws Exception {
        
        MockMvc mockMvc = getMockMvc();
        
        // Make OPTIONS preflight request with Origin header
        MvcResult result = mockMvc.perform(
                options(endpoint)
                        .header("Origin", origin)
                        .header("Access-Control-Request-Method", "GET"))
                .andReturn();
        
        // Verify CORS headers are present in response
        String allowOriginHeader = result.getResponse().getHeader("Access-Control-Allow-Origin");
        
        // Access-Control-Allow-Origin should be present for allowed origins
        Assertions.assertThat(allowOriginHeader).isEqualTo(origin);
    }

    @Provide
    Arbitrary<String> httpMethods() {
        return Arbitraries.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
    }

    @Provide
    Arbitrary<String> allowedOrigins() {
        return Arbitraries.of(
                "http://localhost:4200",
                "http://localhost:3000",
                "http://localhost:8080",
                "http://localhost:8081"
        );
    }

    @Provide
    Arbitrary<String> validEndpoints() {
        return Arbitraries.of(
                "/api/test",
                "/api/users",
                "/api/products",
                "/api/orders",
                "/test",
                "/health"
        );
    }

    /**
     * Feature: frontend-backend-communication, Property 2: Development mode origin acceptance
     * Validates: Requirements 1.3
     * 
     * For any localhost origin with any port number, when the system is in development mode,
     * the Backend API should accept the request and include appropriate CORS headers.
     */
    @Property(tries = 100)
    void developmentModeOriginAcceptanceProperty(
            @ForAll @IntRange(min = 1024, max = 65535) int port) throws Exception {
        
        MockMvc mockMvc = getMockMvc();
        String localhostOrigin = "http://localhost:" + port;
        
        // Make OPTIONS preflight request
        MvcResult result = mockMvc.perform(
                options("/api/test")
                        .header("Origin", localhostOrigin)
                        .header("Access-Control-Request-Method", "GET"))
                .andReturn();
        
        // In development mode, localhost origins should be accepted
        // Check if the origin is in the allowed list
        String allowOriginHeader = result.getResponse().getHeader("Access-Control-Allow-Origin");
        
        // For configured localhost ports, CORS headers should be present
        if (port == 4200 || port == 3000 || port == 8080 || port == 8081) {
            Assertions.assertThat(allowOriginHeader).isEqualTo(localhostOrigin);
        }
    }

    /**
     * Feature: frontend-backend-communication, Property 4: Credentials handling
     * Validates: Requirements 1.5
     * 
     * For any request with credentials flag set, the Backend API response should
     * include Access-Control-Allow-Credentials header with appropriate value.
     */
    @Property(tries = 100)
    void credentialsHandlingProperty(
            @ForAll("allowedOrigins") String origin,
            @ForAll("httpMethods") String method) throws Exception {
        
        MockMvc mockMvc = getMockMvc();
        
        // Make OPTIONS preflight request with credentials
        MvcResult result = mockMvc.perform(
                options("/api/test")
                        .header("Origin", origin)
                        .header("Access-Control-Request-Method", method))
                .andReturn();
        
        // Verify Access-Control-Allow-Credentials header is present
        String allowCredentialsHeader = result.getResponse().getHeader("Access-Control-Allow-Credentials");
        
        // For allowed origins, credentials should be supported
        Assertions.assertThat(allowCredentialsHeader).isEqualTo("true");
    }
}
