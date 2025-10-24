package com.uit_service.se360.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUncatchException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setMessage(ex.getMessage());
    return ResponseEntity.internalServerError().body(errorResponse);
  }
}
