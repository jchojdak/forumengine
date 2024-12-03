package com.forumengine.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<CustomExceptionResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();

        CustomExceptionResponse response = new CustomExceptionResponse();
        response.setPath(request.getRequestURI());
        response.setError(status.name());
        response.setMessage(ex.getMessage());
        response.setStatus(status.value());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<CustomExceptionResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();

        CustomExceptionResponse response = new CustomExceptionResponse();
        response.setPath(request.getRequestURI());
        response.setError(status.name());
        response.setMessage(ex.getMessage());
        response.setStatus(status.value());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();

        CustomExceptionResponse response = new CustomExceptionResponse();
        response.setPath(request.getRequestURI());
        response.setError(status.name());
        response.setMessage(ex.getMessage());
        response.setStatus(status.value());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomExceptionResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();

        CustomExceptionResponse response = new CustomExceptionResponse();
        response.setPath(request.getRequestURI());
        response.setError(status.name());
        response.setMessage(ex.getMessage());
        response.setStatus(status.value());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(CommentNotBelongToPostException.class)
    public ResponseEntity<CustomExceptionResponse> handleCommentNotBelongToPostException(CommentNotBelongToPostException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();

        CustomExceptionResponse response = new CustomExceptionResponse();
        response.setPath(request.getRequestURI());
        response.setError(status.name());
        response.setMessage(ex.getMessage());
        response.setStatus(status.value());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<CustomExceptionResponse> handleInvalidPasswordException(InvalidPasswordException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();

        CustomExceptionResponse response = new CustomExceptionResponse();
        response.setPath(request.getRequestURI());
        response.setError(status.name());
        response.setMessage(ex.getMessage());
        response.setStatus(status.value());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(RoleNotAssignedException.class)
    public ResponseEntity<CustomExceptionResponse> handleRoleNotAssignedException(RoleNotAssignedException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();

        CustomExceptionResponse response = new CustomExceptionResponse();
        response.setPath(request.getRequestURI());
        response.setError(status.name());
        response.setMessage(ex.getMessage());
        response.setStatus(status.value());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(RoleAlreadyAssignedException.class)
    public ResponseEntity<CustomExceptionResponse> handleRoleAlreadyAssignedException(RoleAlreadyAssignedException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();

        CustomExceptionResponse response = new CustomExceptionResponse();
        response.setPath(request.getRequestURI());
        response.setError(status.name());
        response.setMessage(ex.getMessage());
        response.setStatus(status.value());

        return new ResponseEntity<>(response, status);
    }
}
