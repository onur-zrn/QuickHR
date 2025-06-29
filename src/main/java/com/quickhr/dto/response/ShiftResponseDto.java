package com.quickhr.dto.response;

import java.time.*;
import java.util.*;

public record ShiftResponseDto(
        Long shiftId,
        Set<Long> assignedUserIds,
        String shiftName,
        LocalTime beginHour,
        LocalTime endHour
) {
}