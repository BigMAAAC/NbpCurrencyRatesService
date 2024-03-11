package eu.trans.api.v1.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handleException(final Exception e) {
        log.warn("Unexpected exception occurred ", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage() + " " + e.getCause().getMessage());
    }

    @ExceptionHandler(CurrencyRateRetrievalException.class)
    public ResponseEntity<Object> handleCurrencyRateRetrievalException(CurrencyRateRetrievalException e) {
        log.warn("Error retrieving currency rate:", e);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Error retrieving currency rate");
        body.put("details", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<String> handleMissingServletRequestParameter(final MissingServletRequestParameterException e) {
        log.warn("Unexpected exception occurred", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<String> handleTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        final String responseDescription = String.format("Method argument %s has invalid type. detail message = %s", e.getName(), e.getMessage());
        log.warn("MethodArgumentTypeMismatchException: {}", responseDescription);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseDescription);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final String responseDescription = prepareConstraintViolationDescription(e);
        log.warn("MethodArgumentNotValidException: {}", responseDescription);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseDescription);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<String> handleMethodArgumentNotValidException(final HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    ResponseEntity<String> handleNoHandlerFoundException(final NoHandlerFoundException e) {
        final String errorMessage = "Hello, PosManager here. Requested resource ("
                + e.getHttpMethod() + " " + e.getRequestURL() + ") not found or module is disabled.";
        log.warn("NoHandlerFoundException: {}", errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentConversionNotSupportedException.class)
    ResponseEntity<String> handleTypeMismatchException(final MethodArgumentConversionNotSupportedException e) {
        final String responseDescription = String.format("Method argument %s has invalid type. detail message = %s", e.getName(), e.getMessage());
        log.warn("MethodArgumentConversionNotSupportedException: {}", responseDescription);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseDescription);
    }

    private String prepareConstraintViolationDescription(final MethodArgumentNotValidException e) {
        final BindingResult bindingResult = e.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(",", "errorList: [", "]"));
    }

    private String formatFieldError(final FieldError fieldError) {
        return "{\"field\":\"" + fieldError.getField() + "\",\"value\":\"" + fieldError.getRejectedValue() + "\",\"error\":\"" + fieldError.getDefaultMessage() + "\"}";
    }
}