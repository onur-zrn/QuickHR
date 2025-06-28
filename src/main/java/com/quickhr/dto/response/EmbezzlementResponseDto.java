package com.quickhr.dto.response;

import com.quickhr.enums.embezzlement.EEmbezzlementType;

import java.time.LocalDate;

public record EmbezzlementResponseDto (
        String name,
        String description,
        Long embezzlementId,
        Long companyId,
        LocalDate assignedDate,
        EEmbezzlementType embezzlementType,
        String rejectReason

) {
}