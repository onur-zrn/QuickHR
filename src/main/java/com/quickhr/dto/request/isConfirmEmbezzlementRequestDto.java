package com.quickhr.dto.request;

public record isConfirmEmbezzlementRequestDto(
        Long id,
        Boolean isConfirm,
        String rejectReason // sadece isConfirm = false ise geçerli olacak
) {
}