package com.forumengine.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE = "User %s already exists";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public UserAlreadyExistsException(String mes) {
        super(MESSAGE.formatted(mes));
    }

    public HttpStatus getStatus() {
        return STATUS;
    }
}
