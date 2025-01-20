package com.commerce.commerce.controllers;

import com.commerce.commerce.Models.User;
import com.commerce.commerce.Service.AuthenticationService;
import com.commerce.commerce.Service.JwtService;
import com.commerce.commerce.dtos.LoginUserDto;
import com.commerce.commerce.dtos.RegisterUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        long expiresIn = jwtService.getExpirationTime();

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("expiresIn", expiresIn);

        return ResponseEntity.ok(response);
    }
}

