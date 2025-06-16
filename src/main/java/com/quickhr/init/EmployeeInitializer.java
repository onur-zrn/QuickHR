package com.quickhr.init;

import com.quickhr.entity.Employee;
import com.quickhr.enums.user.*;
import java.time.*;
import java.util.*;

public class EmployeeInitializer {
    public static List<Employee> employeeInitializer() {
        List<Employee> employees = new ArrayList<>();

        Map<Long, String> companyDomains = Map.of(
                1L, "technova.com",
                2L, "greensolutions.com",
                3L, "skynet.com",
                4L, "futuresoft.io",
                5L, "urbantech.co",
                6L, "ecoware.org",
                7L, "neonedge.ai",
                8L, "solarcore.com",
                9L, "codenation.dev",
                10L, "pixelpeak.com"
        );

        int employeeCounter = 1;
        Random random = new Random();

        while (employeeCounter <= 50) {
            long companyId = ((employeeCounter - 1) % 10) + 1;  // Döngüsel companyId 1-10

            String domain = companyDomains.get(companyId);

            EGender gender = EGender.values()[random.nextInt(EGender.values().length)];
            EMaritalStatus maritalStatus = EMaritalStatus.values()[random.nextInt(EMaritalStatus.values().length)];
            EEducationLevel educationLevel = EEducationLevel.values()[random.nextInt(EEducationLevel.values().length)];
            EWorkType workType = EWorkType.values()[random.nextInt(EWorkType.values().length)];
            EEmploymentStatus employmentStatus = EEmploymentStatus.values()[random.nextInt(EEmploymentStatus.values().length)];

            String position = employeeCounter <= 10 ? "Manager" : "Employee";
            LocalDate dateOfEmployment = LocalDate.now().minusYears(random.nextInt(10)).minusMonths(random.nextInt(12));
            Double salary = employeeCounter <= 10 ? (10000.0 + companyId * 1000) : (4000.0 + employeeCounter * 100);
            LocalDate dateOfBirth = employeeCounter <= 10
                    ? LocalDate.of(1980 + (int) companyId % 10, 1, 1)
                    : LocalDate.of(1985 + (employeeCounter % 15), 1, 1);
            String firstName = employeeCounter <= 10 ? "ManagerFirstName" + companyId : "FirstName" + employeeCounter;
            String lastName = employeeCounter <= 10 ? "ManagerLastName" + companyId : "LastName" + employeeCounter;
            String mail = employeeCounter <= 10 ? "manager" + companyId + "@" + domain : "user" + employeeCounter + "@" + domain;
            String phone = employeeCounter <= 10 ? "0500111" + String.format("%04d", companyId) : "0500" + String.format("%07d", employeeCounter * 222);
            String identityNumber = "ID" + String.format("%09d", employeeCounter);
            String address = employeeCounter <= 10 ? "Company Address " + companyId : "Address " + employeeCounter;

            employees.add(Employee.builder()
                    .userId((long) employeeCounter)
                    .companyId(companyId)
                    .firstName(firstName)
                    .lastName(lastName)
                    .mail(mail)
                    .phone(phone)
                    .identityNumber(identityNumber)
                    .dateOfBirth(dateOfBirth)
                    .address(address)
                    .gender(gender)
                    .maritalStatus(maritalStatus)
                    .educationLevel(educationLevel)
                    .position(position)
                    .workType(workType)
                    .dateOfEmployment(dateOfEmployment)
                    .dateOfTermination(null)
                    .salary(salary)
                    .employmentStatus(employmentStatus)
                    .build());

            employeeCounter++;
        }

        return employees;
    }
}
