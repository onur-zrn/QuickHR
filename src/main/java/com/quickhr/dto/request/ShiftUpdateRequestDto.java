package com.quickhr.dto.request;

import java.time.*;

public record ShiftUpdateRequestDto(
        String shiftName,
        Integer capacity,
        LocalTime beginHour,
        LocalTime endHour,
        String description

) {
}