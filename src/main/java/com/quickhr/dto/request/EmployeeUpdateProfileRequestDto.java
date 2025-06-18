package com.quickhr.dto.request;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.quickhr.constant.RegexConstants.PHONE_REGEX_E164;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeUpdateProfileRequestDto {
    @Pattern(
            regexp = PHONE_REGEX_E164,
            message = "Telefon numarası uluslararası formatta olmalıdır. Örn: +905xxxxxxxxx"
    )
    private String phone;

    private String address;

}
