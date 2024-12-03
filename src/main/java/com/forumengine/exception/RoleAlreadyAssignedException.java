package com.forumengine.exception;

import org.springframework.http.HttpStatus;

public class RoleAlreadyAssignedException extends RuntimeException {

    private static final String MESSAGE = "Role with ID %s has already been assigned to user with ID %s.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public RoleAlreadyAssignedException(Long userId, Long roleId) {
        super(MESSAGE.formatted(roleId, userId));
    }

    public HttpStatus getStatus() {
        return STATUS;
    }
}
