package com.uit_service.se360.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "Full name must not be blank")
    private String fullName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Phone number must not be blank")
    private String phone;

    @NotBlank(message = "Password must not be blank")
    private String password;
}
