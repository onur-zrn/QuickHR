package com.quickhr.dto.response;

import com.quickhr.enums.user.*;

import java.time.LocalDate;

public record EmployeeDetailsResponseDto(
        String firstName,
        String lastName,
        String identityNumber,
        LocalDate dateOfBirth,
        String phone,
        String address,
        EGender gender,
        EMaritalStatus maritalStatus,
        String mail,
        EEducationLevel educationLevel,
        String position,
        EWorkType workType,
        LocalDate dateOfEmployment,
        LocalDate dateOfTermination,
        Double salary,
        EUserState userState
        //EEmploymentStatus employmentStatus
) {

}