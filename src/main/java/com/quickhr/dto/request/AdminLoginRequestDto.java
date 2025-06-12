package com.quickhr.dto.request;

import jakarta.validation.constraints.*;

public record AdminLoginRequestDto(
        @NotBlank(message = "Username cannot empty!")
        String username,

        @NotBlank(message = "Password cannot empty!")
        String password
) {

}