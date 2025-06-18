package com.quickhr.dto.response;

import com.quickhr.enums.user.EUserState;

public record PersonalStateResponseDto(
        Long id,
        String firstName,
        String lastName,
        EUserState userState
) {

}
