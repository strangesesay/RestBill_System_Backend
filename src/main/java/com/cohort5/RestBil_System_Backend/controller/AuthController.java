package com.cohort5.RestBil_System_Backend.controller;

import com.cohort5.RestBil_System_Backend.payload.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // (Autowired dependencies: UserService, AuthenticationManager, JwtUtils)

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // 1. Authenticate user credentials
        // 2. Generate JWT
        // 3. Return JWT token and user role
        // Example: return ResponseEntity.ok(new JwtResponse(jwt, user.getRole().name()));
        return null; // Placeholder
    }

    @PostMapping("/register/owner")
    public ResponseEntity<?> registerOwner(@RequestBody User user) {
        // 1. Check if an Owner already exists (optional, for first-time setup)
        // 2. Encode password
        // 3. Set user.setRole(Role.OWNER);
        // 4. Save user
        return ResponseEntity.ok("Owner registered successfully!");
    }


    @PostMapping("/register/cashier")
    @PreAuthorize("hasAuthority('OWNER')") // Spring Security enforces this role check
    public ResponseEntity<?> registerCashier(@RequestBody User user) {

        return ResponseEntity.ok("Cashier registered successfully!");
    }
}
