package com.example.ecommerce.config;

import com.example.ecommerce.enums.UserRole;
import com.example.ecommerce.security.JwtAuthenticationFilter;
import com.example.ecommerce.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService   customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()

                        // Product endpoints - POST, PUT, DELETE restricted to VENDOR
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("VENDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("VENDOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("VENDOR")

                        // Address endpoints
                        .requestMatchers("/api/address/**").permitAll()

                        // Product endpoints - GET (and other methods) open to all
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/api/order-item/**").authenticated()

                        // Category endpoints open
                        .requestMatchers("/api/category/**", "/api/category").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}



// 11. Example Protected Controller
//@RestController
//@RequestMapping("/api/protected")
//public class ProtectedController {
//
//    @GetMapping("/user")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseEntity<String> userEndpoint(Authentication authentication) {
//        return ResponseEntity.ok("Hello " + authentication.getName() + "! This is a protected endpoint.");
//    }
//
//    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<String> adminEndpoint(Authentication authentication) {
//        return ResponseEntity.ok("Hello Admin " + authentication.getName() + "!");
//    }
//}