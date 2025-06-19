package com.quickhr.dto.response;

public record AnnualLeaveDetailsDto(
		int totalLeave,
		int usedLeave,
		int remainingLeave
) {

}
