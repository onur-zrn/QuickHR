package com.quickhr.dto.response;

import com.quickhr.enums.user.EUserRole;

public record LoginResponseDto(
		String accessToken,
		String refreshToken,
		EUserRole role
) {

}
