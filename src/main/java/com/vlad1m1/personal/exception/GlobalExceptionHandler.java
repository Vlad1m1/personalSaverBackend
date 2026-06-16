package com.vlad1m1.personal.exception;

import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.ApiErrorResponse.ApiErrorDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<ApiErrorDetail> details = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Validation error", request, details);
    }

    @ExceptionHandler(ApiValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleApiValidation(ApiValidationException exception, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage(), request, toDetails(exception.getDetails()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Invalid request parameter", request,
                List.of(new ApiErrorDetail(exception.getName(), "Invalid value: " + exception.getValue())));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParameter(MissingServletRequestParameterException exception, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Missing required request parameter", request,
                List.of(new ApiErrorDetail(exception.getParameterName(), "Required query parameter is missing")));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableMessage(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Invalid JSON request body", request,
                List.of(new ApiErrorDetail("body", "Request body could not be parsed")));
    }

    @ExceptionHandler(AdminUnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleAdminUnauthorized(AdminUnauthorizedException exception, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(ApiConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ApiConflictException exception, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException exception, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "Resource state conflicts with the requested operation", request, List.of());
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleRateLimit(RateLimitExceededException exception, HttpServletRequest request) {
        return build(HttpStatus.TOO_MANY_REQUESTS, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request, List.of());
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, HttpServletRequest request, List<ApiErrorDetail> details) {
        ApiErrorResponse body = new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                requestId(request),
                details
        );
        return ResponseEntity.status(status).body(body);
    }

    private List<ApiErrorDetail> toDetails(Map<String, String> details) {
        return details.entrySet().stream()
                .map(entry -> new ApiErrorDetail(entry.getKey(), entry.getValue()))
                .toList();
    }

    private String requestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-Id");
        if (requestId == null || requestId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return requestId;
    }
}
