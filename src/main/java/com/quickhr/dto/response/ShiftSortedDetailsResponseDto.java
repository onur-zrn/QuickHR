
package com.quickhr.dto.response;

import java.time.*;
import java.util.*;

public record ShiftSortedDetailsResponseDto(
        Long companyId,
        Long shiftId,
        Integer capacity,
        String shiftName,
        LocalTime beginHour,
        LocalTime endHour,
        String description,
        List<Long> assignedUserIds,
        List<String> fullNames

) {
}