package com.example.ecommerce.service;

import com.example.ecommerce.dto.reponse.AuthResponse;
import com.example.ecommerce.dto.request.LoginRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.UserRole;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(LoginRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);
        userRepository.save(user);
//
//        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
//                .username(user.getEmail())
//                .password(user.getPassword())
//                .authorities("ROLE_" + user.getRole().name())
//                .build();
//
//        String jwtToken = jwtService.generateToken(userDetails);
//        return new AuthResponse(jwtToken, user.getEmail());
        return new AuthResponse("User registered successfully", user.getEmail());
    }

    public AuthResponse authenticate(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
//
//        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
//                .username(user.getEmail())
//                .password(user.getPassword())
//                .authorities("ROLE_" + user.getRole().name())
//                .build();
//
//        String jwtToken = jwtService.generateToken(userDetails);
        return new AuthResponse("Login successful", user.getEmail());
    }

    public String generateTokenForUser(String email){
        User user = userRepository.findByEmail(email).orElseThrow();

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();

        return jwtService.generateToken(userDetails);
    }
}
