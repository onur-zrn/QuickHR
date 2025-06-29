package com.quickhr.dto.response;

import java.time.*;
import java.util.*;

public record ShiftDetailsResponseDto(
        Long companyId,
        Long shiftId,
        Integer capacity,
        String shiftName,
        LocalTime beginHour,
        LocalTime endHour,
        String description,
        Set<Long> assignedUserIds,
        Set<String> fullNames

) {
}
