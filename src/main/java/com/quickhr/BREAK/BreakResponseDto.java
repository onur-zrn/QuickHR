package com.quickhr.BREAK;

import java.time.*;

public record BreakResponseDto(
		Long breakId,
		Long userId,
		EBreakType breakType,
		LocalTime beginTime,
		LocalTime endTime
) {
}
