package ru.practicum.explore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.dto.ApiError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<ApiError> handleUnitNotFound(UnitNotFoundException e) {
        log.info("Ошибка {}: {}", e.getClass().getSimpleName(), e.getMessage());
        ApiError apiError = ApiError.builder()
                .reason("The required object was not found.")
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::getClassName)
                        .collect(Collectors.toList()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleInitiatorIsOwner(InitiatorIsOwnerException e) {
        log.info("Ошибка {}: {}", e.getClass().getSimpleName(), e.getMessage());
        ApiError apiError = ApiError.builder()
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::getClassName)
                        .collect(Collectors.toList()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleRequestAlreadyExists(RequestAlreadyExistsException e) {
        log.info("Ошибка {}: {}", e.getClass().getSimpleName(), e.getMessage());
        ApiError apiError = ApiError.builder()
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::getClassName)
                        .collect(Collectors.toList()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleRequestsLimit(RequestsLimitException e) {
        log.info("Ошибка {}: {}", e.getClass().getSimpleName(), e.getMessage());
        ApiError apiError = ApiError.builder()
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::getClassName)
                        .collect(Collectors.toList()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleUnavailableOperation(UnavailableOperationException e) {
        log.info("Ошибка {}: {}", e.getClass().getSimpleName(), e.getMessage());
        ApiError apiError = ApiError.builder()
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::getClassName)
                        .collect(Collectors.toList()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleWrongDate(WrongDateException e) {
        log.info("Ошибка {}: {}", e.getClass().getSimpleName(), e.getMessage());
        ApiError apiError = ApiError.builder()
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::getClassName)
                        .collect(Collectors.toList()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleWrongOwner(WrongOwnerException e) {
        log.info("Ошибка {}: {}", e.getClass().getSimpleName(), e.getMessage());
        ApiError apiError = ApiError.builder()
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::getClassName)
                        .collect(Collectors.toList()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleWrongState(WrongStateException e) {
        log.info("Ошибка {}: {}", e.getClass().getSimpleName(), e.getMessage());
        ApiError apiError = ApiError.builder()
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::getClassName)
                        .collect(Collectors.toList()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<List<ApiError>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.info("Ошибка {}: {}", e.getClass().getSimpleName(), e.getMessage());
        List<ApiError> apiErrors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            ApiError apiError = ApiError.builder()
                    .reason("For the requested operation the conditions are not met.")
                    .message(error.getDefaultMessage())
                    .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .timestamp(LocalDateTime.now())
                    .errors(Arrays.stream(e.getStackTrace())
                            .map(StackTraceElement::getClassName)
                            .collect(Collectors.toList()))
                    .build();
            apiErrors.add(apiError);
        });
        return new ResponseEntity<>(apiErrors, HttpStatus.BAD_REQUEST);
    }
}