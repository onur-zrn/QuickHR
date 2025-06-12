package com.quickhr.dto.request;

public record MailVerifyCodeRequestDto(
		//Long id, id ye token dan ulaşıyoruz
		String verificationCode
) {

}
