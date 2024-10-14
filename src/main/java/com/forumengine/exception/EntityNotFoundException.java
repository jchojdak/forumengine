package com.forumengine.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends RuntimeException {

    private static final String MESSAGE = "%s not found";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public EntityNotFoundException(String mess) {
        super(MESSAGE.formatted(mess));
    }

    public HttpStatus getStatus() {
        return STATUS;
    }

}
