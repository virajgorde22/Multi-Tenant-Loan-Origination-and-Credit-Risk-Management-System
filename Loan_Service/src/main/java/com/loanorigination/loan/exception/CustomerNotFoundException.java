package com.loanorigination.loan.exception;

public class CustomerNotFoundException
        extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}