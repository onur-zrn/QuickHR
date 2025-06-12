package com.quickhr.dto.request;

import jakarta.validation.constraints.*;

public record UserLoginRequestDto(
		@NotBlank(message = "E-mail cannot empty!")
		@Email(message = "Enter valid e-mail address!")
		String mail,

		@NotBlank(message = "Password cannot empty!")
		String password

) {

}