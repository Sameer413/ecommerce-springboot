package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.AuthResponse;
import com.example.ecommerce.dto.request.LoginRequest;
import com.example.ecommerce.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

//    @PostMapping("/register")
//    public ResponseEntity<AuthResponse> register(@RequestBody LoginRequest request){
//        return ResponseEntity.ok(authService.register(request));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
//        return ResponseEntity.ok(authService.register(request));
//    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.register(request);

        // Generate token and set as cookie
        String token = authService.generateTokenForUser(request.getEmail());
        setJwtCookie(response, token);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.authenticate(request);

        // Generate token and set as cookie
        String token = authService.generateTokenForUser(request.getEmail());
        setJwtCookie(response, token);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        // Clear the JWT cookie
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Set to true in production with HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire immediately
        response.addCookie(cookie);

        Map<String, String> result = new HashMap<>();
        result.put("message", "Logged out successfully");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Map<String, String> result = new HashMap<>();
            result.put("email", authentication.getName());
            result.put("role", authentication.getAuthorities().toString());
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(401).build();
    }

    private void setJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true); // Prevents XSS attacks
        cookie.setSecure(false); // Set to true in production with HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 24 hours in seconds
        // cookie.setSameSite("Strict"); // Uncomment for additional CSRF protection
        response.addCookie(cookie);
    }
}
