package com.quickhr.dto.request;

import com.quickhr.enums.company.*;
import jakarta.validation.constraints.*;

public record ChangeCompanyStatusRequestDto(
        @NotBlank(message = "Company ID cannot empty!")
        Long companyId,

        @NotBlank(message = "New status must be one of: PENDING, ACCEPTED, DELETED, DENIED")
        ECompanyState newStatus
) {

}
