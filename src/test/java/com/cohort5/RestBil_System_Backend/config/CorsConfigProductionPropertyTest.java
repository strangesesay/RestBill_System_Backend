package com.cohort5.RestBil_System_Backend.config;

import net.jqwik.api.*;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Property-based tests for CORS configuration in production mode.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("prod")
class CorsConfigProductionPropertyTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc getMockMvc() {
        return MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Feature: frontend-backend-communication, Property 3: Production mode origin restriction
     * Validates: Requirements 1.4
     * 
     * For any origin that is not in the configured trusted origins list,
     * when the system is in production mode, the Backend API should reject
     * the request or omit CORS headers.
     */
    @Property(tries = 100)
    void productionModeOriginRestrictionProperty(
            @ForAll("untrustedOrigins") String origin) throws Exception {
        
        MockMvc mockMvc = getMockMvc();
        
        // Make OPTIONS preflight request from untrusted origin
        MvcResult result = mockMvc.perform(
                options("/api/test")
                        .header("Origin", origin)
                        .header("Access-Control-Request-Method", "GET"))
                .andReturn();
        
        // Untrusted origins should not receive CORS headers
        String allowOriginHeader = result.getResponse().getHeader("Access-Control-Allow-Origin");
        
        // The origin should either be null or not match the untrusted origin
        Assertions.assertThat(allowOriginHeader).isNotEqualTo(origin);
    }

    @Provide
    Arbitrary<String> untrustedOrigins() {
        return Arbitraries.of(
                "http://malicious.com",
                "http://untrusted.example.com",
                "http://localhost:4200",  // localhost not allowed in prod
                "http://localhost:3000",
                "https://random-site.com",
                "http://evil.org"
        );
    }
}
