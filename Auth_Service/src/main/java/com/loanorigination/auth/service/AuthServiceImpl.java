package com.loanorigination.auth.service;

import com.loanorigination.auth.dto.AuthResponse;
import com.loanorigination.auth.dto.LoginRequest;
import com.loanorigination.auth.dto.RegisterRequest;
import com.loanorigination.auth.entity.User;
import com.loanorigination.auth.exception.InvalidCredentialsException;
import com.loanorigination.auth.exception.UserAlreadyExistsException;
import com.loanorigination.auth.exception.UserNotFoundException;
import com.loanorigination.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User already exists with email: " + request.getEmail());
        }

        User user = User.builder()
                .tenantId(request.getTenantId())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword()) // Later replace with BCrypt
                .role(request.getRole())
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(user);

        String token = jwtService.generateToken(user.getUsername());

        return AuthResponse.builder()
                .message("Registration Successful")
                .token(token)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + request.getEmail()));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException(
                    "Invalid email or password");
        }

        String token = jwtService.generateToken(user.getUsername());

        return AuthResponse.builder()
                .message("Login Successful")
                .token(token)
                .build();
    }
}