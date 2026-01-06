package com.se360.UIT_Go.driver_service.exceptions;

import com.se360.UIT_Go.driver_service.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerException {
    // Xử lý các exception chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUncaughtException(Exception ex) {
        var errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    // Xử lý validation exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex) {

        var fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, other) -> existing
                ));


        var response = new ApiResponse<Void>();
        response.setCode(ErrorCode.VALIDATION_ERROR.getCode());
        response.setMessage(ErrorCode.VALIDATION_ERROR.getMessage());
        response.setErrors(fieldErrors);

        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getHttpCode())
                .body(response);
    }

}
