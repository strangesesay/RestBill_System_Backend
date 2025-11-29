package com.cohort5.RestBil_System_Backend.controller;

import com.cohort5.RestBil_System_Backend.Model.Role;
import com.cohort5.RestBil_System_Backend.Model.User;
import com.cohort5.RestBil_System_Backend.payload.JwtResponse;
import com.cohort5.RestBil_System_Backend.payload.LoginRequest;
import com.cohort5.RestBil_System_Backend.payload.RegisterRequest;
import com.cohort5.RestBil_System_Backend.security.JwtUtil;
import com.cohort5.RestBil_System_Backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByUsername(userDetails.getUsername());
            
            String jwt = jwtUtil.generateToken(userDetails, user.getRole().name());

            return ResponseEntity.ok(new JwtResponse(jwt, user.getUsername(), user.getRole().name()));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid username or password");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/register/cashier")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<?> registerCashier(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User cashier = userService.createUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                Role.CASHIER
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "Cashier registered successfully!");
            response.put("username", cashier.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
