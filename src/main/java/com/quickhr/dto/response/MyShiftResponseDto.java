package com.quickhr.dto.response;

import java.time.*;

public record MyShiftResponseDto(
        Long shiftId,
        String shiftName,
        LocalTime beginHour,
        LocalTime endHour

) {
}
