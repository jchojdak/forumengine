package com.forumengine.exception;

import org.springframework.http.HttpStatus;

public class EntityAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE = "%s already exists";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public EntityAlreadyExistsException(String mess) {
        super(MESSAGE.formatted(mess));
    }

    public HttpStatus getStatus() {
        return STATUS;
    }

}
