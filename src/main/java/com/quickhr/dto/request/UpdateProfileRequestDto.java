package com.quickhr.dto.request;

public record UpdateProfileRequestDto(
        String firstName,
        String lastName,
        String phone,
        String avatar
) {
}
