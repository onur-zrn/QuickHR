package com.quickhr.dto.request;

import com.quickhr.enums.embezzlement.EEmbezzlementType;

public record CreateEmbezzlementRequestDto(
        String name,
        String brand,
        String model,
        String serialNumber,
        String description,
        EEmbezzlementType embezzlementType
) {
}
