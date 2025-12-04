package com.se360.UIT_Go.trip_service.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ErrorResponse {
  private String message;
  private Map<String, String> details;
}
