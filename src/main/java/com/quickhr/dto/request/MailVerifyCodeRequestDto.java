package com.quickhr.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MailVerifyCodeRequestDto(
		//Long id, id ye token dan ulaşıyoruz
		@NotBlank(message = "Verification code cannot empty!")
		String verificationCode
) {

}
