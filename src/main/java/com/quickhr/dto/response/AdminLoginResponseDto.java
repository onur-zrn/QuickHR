package com.quickhr.dto.response;

import com.quickhr.enums.*;

public record AdminLoginResponseDto(
        String token,
        EAdminRole role
) {

}