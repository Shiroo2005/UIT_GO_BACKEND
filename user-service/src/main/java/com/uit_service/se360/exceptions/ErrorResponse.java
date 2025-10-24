package com.uit_service.se360.exceptions;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
  private String message;
  private Map<String, String> details;
}
