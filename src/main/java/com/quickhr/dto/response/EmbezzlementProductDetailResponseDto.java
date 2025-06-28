package com.quickhr.dto.response;

import com.quickhr.enums.embezzlement.EEmbezzlementState;
import com.quickhr.enums.embezzlement.EEmbezzlementType;

import java.time.LocalDate;
/*
  Kullanıcıya zimmet listesi
 */
public record EmbezzlementProductDetailResponseDto(
        Long userId,
        String fullName,
        Long embezzlementId,
        String name,
        String brand,
        String model,
        String serialNumber,
        EEmbezzlementType embezzlementType,
        EEmbezzlementState embezzlementState,
        LocalDate assignedDate,
        LocalDate returnedDate,
        String rejectReason
) {
}
