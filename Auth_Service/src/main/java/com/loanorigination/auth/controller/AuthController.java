package com.loanorigination.auth.controller;

import com.loanorigination.auth.dto.AuthResponse;
import com.loanorigination.auth.dto.LoginRequest;
import com.loanorigination.auth.dto.RegisterRequest;
import com.loanorigination.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public AuthResponse register(
            @RequestBody RegisterRequest request){

        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request){

        return authService.login(request);
    }

}