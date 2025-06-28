package com.quickhr.dto.request;


import com.quickhr.enums.embezzlement.EEmbezzlementState;

import java.time.LocalDate;
/*
   Zimmetin durumunu güncelleme
 */
public record UpdateEmbezzlementProductRequestDto(
        String description,
        Long userId,
        LocalDate assignedDate,
        LocalDate returnedDate,
        EEmbezzlementState embezzlementState

) {
}
