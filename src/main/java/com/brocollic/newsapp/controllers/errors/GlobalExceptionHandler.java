package com.brocollic.newsapp.controllers.errors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //CONSIDER ADDING HANDLER FOR:
    //IllegalArgumentException - when query param of id type is null, calls findById(null)
    //ExpiredJwtException - for when jwt is expired
    //HttpMessageNotReadableException - when json is malformed and cannot be deserialized

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<Object> handleEntityNotFound(NoSuchElementException ex, HttpServletRequest request) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message("Resource " + request.getRequestURI() + " does not exist.")
                .httpStatus(HttpStatus.NOT_FOUND)
                .time(LocalDateTime.now())
                .build();
        return buildErrorResponse(errorMessage);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
                                                                        HttpServletRequest request) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message("Unable to create resource: " + ex.getMostSpecificCause().getMessage())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .time(LocalDateTime.now())
                .build();
        return buildErrorResponse(errorMessage);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatusCode status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message("Validation error")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .time(LocalDateTime.now())
                .errors(fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()))
                .build();

        return buildErrorResponse(errorMessage);
    }

    private ResponseEntity<Object> buildErrorResponse(ErrorMessage errorMessage) {
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }
}
