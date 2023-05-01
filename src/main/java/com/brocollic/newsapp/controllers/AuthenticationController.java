package com.brocollic.newsapp.controllers;

import com.brocollic.newsapp.dtos.requests.UserCreateRequest;
import com.brocollic.newsapp.dtos.requests.UserLoginRequest;
import com.brocollic.newsapp.dtos.responses.TokenResponse;
import com.brocollic.newsapp.entities.Role;
import com.brocollic.newsapp.entities.User;
import com.brocollic.newsapp.services.UserService;
import com.brocollic.newsapp.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/register",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        User newUser = User.builder()
                .email(userCreateRequest.getEmail())
                .firstname(userCreateRequest.getFirstname())
                .lastname(userCreateRequest.getLastname())
                .password(passwordEncoder.encode(userCreateRequest.getPassword()))
                .role(Role.DEFAULT_USER).build();
        userService.save(newUser);
        String token = jwtUtil.generateToken(newUser);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping(value = "/login",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        User user =  userService.findByEmail(userLoginRequest.getEmail()).orElseThrow();
        TokenResponse token = TokenResponse.builder()
                .token(jwtUtil.generateToken(user)).build();
        return ResponseEntity.ok().body(token);
    }

}
