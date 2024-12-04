package com.forumengine.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends RuntimeException {

    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    public AccessDeniedException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return STATUS;
    }

}
