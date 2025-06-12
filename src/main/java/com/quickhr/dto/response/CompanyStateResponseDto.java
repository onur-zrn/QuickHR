package com.quickhr.dto.response;

import com.quickhr.enums.company.*;

public record CompanyStateResponseDto(
        Long id,
        String name,
        ECompanyState companyState
) {

}
