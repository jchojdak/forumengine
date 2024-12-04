package com.forumengine.exception;

import org.springframework.http.HttpStatus;

public class RoleNotAssignedException extends RuntimeException {

    private static final String MESSAGE = "User with ID %s does not have the role with ID %s.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public RoleNotAssignedException(Long userId, Long roleId) {
        super(MESSAGE.formatted(userId, roleId));
    }

    public HttpStatus getStatus() {
        return STATUS;
    }
}
