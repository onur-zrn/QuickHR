package com.quickhr.BREAK;

import java.time.*;

public record BreakDetailsResponseDto(
		Long breakId,
		Long companyId,
		Long userId,
		String fullName,
		EBreakType breakType,
		LocalTime beginTime,
		LocalTime endTime,
		String description
) {
}
