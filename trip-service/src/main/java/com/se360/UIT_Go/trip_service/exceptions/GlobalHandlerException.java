package com.se360.UIT_Go.trip_service.exceptions;

import com.se360.UIT_Go.trip_service.dtos.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerException {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUncatchException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setMessage(ex.getMessage());
    return ResponseEntity.internalServerError().body(errorResponse);
  }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        var fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, other) -> existing
                ));
        response.setCode(ErrorCode.VALIDATION_ERROR.getCode());
        response.setMessage(ErrorCode.VALIDATION_ERROR.getMessage());
        response.setErrors(fieldErrors);
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getHttpCode()).body(response);
    }

}
