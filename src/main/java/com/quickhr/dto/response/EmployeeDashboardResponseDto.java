package com.quickhr.dto.response;

import java.util.List;

public record EmployeeDashboardResponseDto(
        List<PublicHolidayResponseDto> holidays,
        AnnualLeaveDetailsDto annualLeaveDetails,
        PersonalSpendingSummaryWithTotalResponseDto monthlySpendingSummary,
        List<EmbezzlementProductDetailResponseDto> embezzlementProductDetailResponseDtos,
        MyShiftResponseDto myShiftResponseDto
) {

    public static EmployeeDashboardResponseDto of(
            List<PublicHolidayResponseDto> holidays,
            AnnualLeaveDetailsDto annualLeaveDetails,
            PersonalSpendingSummaryWithTotalResponseDto monthlySpendingSummary,
            List<EmbezzlementProductDetailResponseDto> embezzlementProductDetailResponseDtos,
            MyShiftResponseDto myShiftResponseDto
    ) {
        return new EmployeeDashboardResponseDto(
                holidays != null ? holidays : List.of(),
                annualLeaveDetails,
                monthlySpendingSummary,
                embezzlementProductDetailResponseDtos,
                myShiftResponseDto
        );
    }
}
