package com.quickhr.dto.response;

import java.time.LocalDate;

public record PersonalSpendingSummaryDto(
        Long id,
        LocalDate spendingDate,
        Double billAmount
) {
}
