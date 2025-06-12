package com.quickhr.dto.response;

public record ForgotPasswordResponseDto(
		String token,
		String email,
		String infoMessage
) {
}
