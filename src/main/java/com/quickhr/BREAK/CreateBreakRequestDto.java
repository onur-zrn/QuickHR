package com.quickhr.BREAK;

import jakarta.validation.constraints.*;
import java.time.LocalTime;

public record CreateBreakRequestDto(
		@NotNull
		EBreakType breakType,
		
		@NotNull
		LocalTime beginTime,
		
		@NotNull
		LocalTime endTime,
		
		String description,
		
		@NotNull
		Long userId
) {
}
