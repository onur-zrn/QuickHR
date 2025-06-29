package com.quickhr.dto.request;

import jakarta.validation.constraints.*;

public record AssignShiftRequestDto(
        @NotNull(message = "Shift Id cannot empty!")
        Long shiftId
) {
}
