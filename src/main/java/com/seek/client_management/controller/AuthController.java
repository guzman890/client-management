package com.seek.client_management.controller;

import com.seek.client_management.model.entity.UserEntity;
import com.seek.client_management.security.JWTUtil;
import com.seek.client_management.service.Impl.UserSecurityService;
import com.seek.client_management.service.Impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        final String username = credentials.get("username");
        final String password = credentials.get("password");

        try {
            UserDetails userDetails = userSecurityService.loadUserByUsername(username);

            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                String token = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/api/users")
    public ResponseEntity<UserEntity> registerUser(@RequestBody UserEntity user) {
        UserEntity savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}