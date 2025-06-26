package com.quickhr.dto.request;

import com.quickhr.enums.spendings.ESpendingType;

import java.time.LocalDate;

public record UpdatePersonalSpendingRequestDto(
        String description,
        Double billAmount,
        LocalDate spendingDate,
        ESpendingType spendingType,
        String billDocumentUrl
) {
}