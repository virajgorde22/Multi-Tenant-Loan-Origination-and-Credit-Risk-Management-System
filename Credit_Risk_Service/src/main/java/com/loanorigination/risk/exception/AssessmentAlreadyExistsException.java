package com.loanorigination.risk.exception;

public class AssessmentAlreadyExistsException
        extends RuntimeException {

    public AssessmentAlreadyExistsException(String message) {
        super(message);
    }
}