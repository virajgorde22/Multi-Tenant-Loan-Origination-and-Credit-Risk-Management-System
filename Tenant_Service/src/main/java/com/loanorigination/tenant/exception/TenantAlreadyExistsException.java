package com.loanorigination.tenant.exception;

public class TenantAlreadyExistsException extends RuntimeException {

    public TenantAlreadyExistsException(String message) {
        super(message);
    }

}