package com.forumengine.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends RuntimeException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public InvalidPasswordException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return STATUS;
    }

}
