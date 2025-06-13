package com.quickhr.dto.response;

import com.quickhr.enums.*;

public record AdminLoginResponseDto(
        String accessToken,
        String refreshToken,
        EAdminRole role
) {

}