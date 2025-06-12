package com.quickhr.dto.request;

public record MailSenderRequestDto(
		String mail,
		String activationCode
) {

}
