package org.ifsul.carhired.api.system.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.NonNull;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(@NonNull ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> {
                    String propertyPath = violation.getPropertyPath().toString();
                    String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
                    return fieldName + ": " + violation.getMessage();
                })
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex) {
        List<String> errors = List.of("invalid json format");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        List<String> errors = List.of("invalid query parameters");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(@NonNull EmailAlreadyExistsException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, errors);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(@NonNull BadCredentialsException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, errors);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(@NonNull UsernameNotFoundException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Object> handleSignatureException(@NonNull SignatureException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, errors);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Object> handleSignatureException(@NonNull MalformedJwtException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, errors);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<Object> handleInvalidBearerTokenException(@NonNull InvalidBearerTokenException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, errors);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(@NonNull AccessDeniedException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, errors);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(@NonNull ExpiredJwtException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, errors);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<Object> handleInsufficientAuthenticationException(@NonNull InsufficientAuthenticationException ex) {
        List<String> errors = List.of("Invalid credentials, verify your JWT token");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, errors);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(@NonNull ValidationException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(@NonNull DataIntegrityViolationException ex) {
        var msg = ex.getCause().getMessage();
        List<String> errors = new ArrayList<>();
        Pattern pattern = Pattern.compile("Key \\((.*?)\\)=");
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            var fieldName = matcher.group(1);
            errors = List.of(fieldName + ": is already in use");
        }
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, errors);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(OpenApiResourceNotFoundException.class)
    public ResponseEntity<Object> handleOpenApiResourceNotFoundException(@NonNull OpenApiResourceNotFoundException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(@NonNull BadRequestException ex) {
        List<String> errors = List.of(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(@NonNull Exception ex) {
        List<String> errors = List.of(ex.getMessage(), ex.getClass().toString());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errors);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
