package com.quickhr.dto.request;

public record ChangePasswordRequestDto(
        String oldPassword,
        String newPassword,
        String reNewPassword
) {
}
