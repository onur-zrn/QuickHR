package com.quickhr.dto.response;

import java.util.List;

public record EmployeeDashboardResponseDto(
        List<PublicHolidayResponseDto> holidays,
        AnnualLeaveDetailsDto annualLeaveDetails,
        PersonalSpendingSummaryWithTotalResponseDto monthlySpendingSummary,
        List<EmbezzlementProductDetailResponseDto> embezzlementProductDetailResponseDtos
) {

    public static EmployeeDashboardResponseDto of(
            List<PublicHolidayResponseDto> holidays,
            AnnualLeaveDetailsDto annualLeaveDetails,
            PersonalSpendingSummaryWithTotalResponseDto monthlySpendingSummary,
            List<EmbezzlementProductDetailResponseDto> embezzlementProductDetailResponseDtos

    ) {
        return new EmployeeDashboardResponseDto(
                holidays != null ? holidays : List.of(),
                annualLeaveDetails,
                monthlySpendingSummary,
                embezzlementProductDetailResponseDtos
        );
    }
}
