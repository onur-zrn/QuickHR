package com.quickhr.dto.request;

import jakarta.validation.constraints.NotBlank;

public record isApprovedRequestLeaveRequestDto(
        @NotBlank(message = "ID cannot empty!")
        Long id,

        @NotBlank(message = "IsApproved cannot empty!")
        Boolean isApproved
) {
}
