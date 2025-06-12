package com.quickhr.dto.request;

public record ActivateCodeRequestDto(
		Long id,
		String activationCode
) {

}
