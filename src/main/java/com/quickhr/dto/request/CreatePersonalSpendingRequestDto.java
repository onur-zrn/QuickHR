package com.quickhr.dto.request;

import com.quickhr.enums.spendings.ESpendingType;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreatePersonalSpendingRequestDto(
        @NotBlank(message = "Açıklama boş olamaz.")
        String description,
        @NotNull(message = "Fatura tutarı boş olamaz.")
        @Positive(message = "Fatura tutarı pozitif olmalıdır.")
        Double billAmount,
        @NotNull(message = "Harcama tarihi gereklidir.")
        LocalDate spendingDate,
        @NotNull(message = "Harcama türü gereklidir.")
        ESpendingType spendingType,
        @NotBlank(message = "Fatura belge yolu boş olamaz.")
        String billDocumentUrl
) {
}
