package com.quickhr.dto.request;

import com.quickhr.enums.user.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

import static com.quickhr.constant.RegexConstants.PHONE_REGEX_E164;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeUpdateRequestDto {

    private String firstName;

    private String lastName;

    private String identityNumber;

    private LocalDate dateOfBirth;

    @Pattern(
            regexp = PHONE_REGEX_E164,
            message = "Telefon numarası uluslararası formatta olmalıdır. Örn: +905xxxxxxxxx"
    )
    private String phone;

    private String address;

    private EGender gender;

    private EMaritalStatus maritalStatus;

    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    private String mail;

    private EEducationLevel educationLevel;

    private String position;

    private EWorkType workType;

    private LocalDate dateOfEmployment;

    private LocalDate dateOfTermination;

    @DecimalMin(value = "0.0", inclusive = false, message = "Maaş pozitif bir değer olmalıdır.")
    private Double salary;

    private EEmploymentStatus employmentStatus;

}
