package com.loanorigination.auth.service;

import com.loanorigination.auth.dto.AuthResponse;
import com.loanorigination.auth.dto.LoginRequest;
import com.loanorigination.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}