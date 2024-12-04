package com.forumengine.exception;

import org.springframework.http.HttpStatus;

public class CommentNotBelongToPostException extends RuntimeException {

    private static final String MESSAGE = "The comment %s does not belong to the post %s.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public CommentNotBelongToPostException(Long commentId, Long postId) {
        super(MESSAGE.formatted(commentId, postId));
    }

    public HttpStatus getStatus() {
        return STATUS;
    }

}
