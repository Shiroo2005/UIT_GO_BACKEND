package com.uit_service.se360.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
  @NotBlank private String fullName;
  @NotBlank @Email private String email;
  @NotBlank private String phone;
  @NotBlank private String password;
}
