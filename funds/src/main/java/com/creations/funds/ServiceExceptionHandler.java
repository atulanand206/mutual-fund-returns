package com.creations.funds;

import com.creations.funds.exceptions.ServiceException;
import com.creations.funds.models.ServiceError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import java.time.Instant;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class, ServletException.class})
    ResponseEntity<ServiceError> handleExceptions(
            final Exception ex,
            final WebRequest request) {
        final var rootCause = getExceptionRootCause(ex);
        final var message = rootCause != null ? rootCause.getMessage() : "";
        if (rootCause instanceof ServiceException) {
            return createErrorResponse(message, request, ((ServiceException) ex).getStatus());
        }
        return createErrorResponse(message, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            HttpMessageConversionException.class,
            MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ServiceError> handleBadRequest(
            final RuntimeException ex,
            final WebRequest request) {
        final var rootCause = getExceptionRootCause(ex);
        final var message = rootCause != null ? rootCause.getMessage() : "";
        return createErrorResponse(message, request, HttpStatus.BAD_REQUEST);
    }

    private static Throwable getExceptionRootCause(@Nullable final Throwable cause) {
        if (cause == null)
            return null;
        final var rootCause = ExceptionUtils.getRootCause(cause);
        if (rootCause == null)
            return cause;
        return rootCause;
    }

    private ResponseEntity<ServiceError> createErrorResponse(String message, WebRequest request, HttpStatus statusCode) {
        return ResponseEntity.status(statusCode).body(createError(message, request, statusCode));
    }

    private ServiceError createError(String message, WebRequest request, HttpStatus status) {
        return new ServiceError(status.value(), message,
                Instant.now(), getUri(request), status.getReasonPhrase());
    }

    private static String getUri(final WebRequest request) {
        if (request == null)
            return "empty request";
        if (request instanceof ServletWebRequest)
            return ((ServletWebRequest) request).getRequest().getRequestURI();
        return "unknown path";
    }
}