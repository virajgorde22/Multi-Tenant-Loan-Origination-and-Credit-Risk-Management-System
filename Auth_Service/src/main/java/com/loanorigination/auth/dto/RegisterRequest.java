package com.loanorigination.auth.dto;

import com.loanorigination.auth.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private Long tenantId;

    private String username;

    private String email;

    private String password;

    private Role role;
}