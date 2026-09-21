package com.loanorigination.notification.exception;

public class NotificationNotFoundException
        extends RuntimeException {

    public NotificationNotFoundException(String message) {
        super(message);
    }
}