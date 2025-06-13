package com.quickhr.dto.request;

import jakarta.validation.constraints.*;

import static com.quickhr.constant.RegexConstants.PHONE_REGEX_E164;

public record UpdateProfileRequestDto(
        @Size(max = 50, message = "First name must max 50 characters!")
        String firstName,

        @Size(max = 50, message = "Last name must max 50 characters!")
        String lastName,

        @Pattern(regexp = PHONE_REGEX_E164,
                message = "Phone number must international format!")
        String phone,

        String avatar
) {
}
