package com.quickhr.controller;

import com.quickhr.dto.request.EmployeeRequestDto;
import com.quickhr.dto.response.*;
import com.quickhr.service.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(COMPANY)
@CrossOrigin(origins = "*")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping(COMPANY_DASHBOARD)
    public ResponseEntity<BaseResponse<CompanyDashboardResponseDto>> getCompanyDashboard(@RequestParam String token) {
        CompanyDashboardResponseDto dashboard = companyService.getCompanyDashboard(token);
        return ResponseEntity.ok(BaseResponse.<CompanyDashboardResponseDto>builder()
                .code(200)
                .data(dashboard)
                .success(true)
                .message("Company dashboard data retrieved!")
                .build());
    }

    @GetMapping(PERSONALS)
    public ResponseEntity<BaseResponse<List<EmployeeResponseDto>>> getEmployeeInCompany(@RequestParam String token) {
        List<EmployeeResponseDto> personals = companyService.getEmployeeInCompany(token);

        if (personals == null || personals.isEmpty()) {
            return ResponseEntity.ok(BaseResponse.<List<EmployeeResponseDto>>builder()
                    .code(204)
                    .data(Collections.emptyList())
                    .success(true)
                    .message("No personals found for the company.")
                    .build());
        }

        return ResponseEntity.ok(BaseResponse.<List<EmployeeResponseDto>>builder()
                .code(200)
                .data(personals)
                .success(true)
                .message("Company personals retrieved successfully!")
                .build());
    }
    @GetMapping(PERSONAL_DETAILS)
    public ResponseEntity<BaseResponse<EmployeeResponseDto>> getEmployeeDetailsById(
            @RequestParam String token,
            @PathVariable Long id) {
        EmployeeResponseDto employeeDetails = companyService.getEmployeeDetailsById(token, id);
        return ResponseEntity.ok(BaseResponse.<EmployeeResponseDto>builder()
                .code(200)
                .data(employeeDetails)
                .success(true)
                .message("Employee details retrieved!")
                .build());

    }

    /**
     * Şirkete yeni personel ekler ve oluşan personeli döner.
     *
     * @param token JWT Bearer token (Authorization header)
     * @param dto Personel oluşturma isteği
     * @return Oluşturulan personel bilgileri
     */
    @PostMapping(ADD_PERSONAL)
    public ResponseEntity<BaseResponse<EmployeeResponseDto>> addPersonal(
            //@RequestHeader("Authorization") String token,
            @RequestHeader String token,
            @Valid @RequestBody EmployeeRequestDto dto) {

        // Service katmanında token’dan companyId ve userId alınır, personel eklenir
        EmployeeResponseDto responseDto = companyService.addEmployee(token, dto);         // Service katmanında token’dan companyId ve userId alınır, personel eklenir


        return ResponseEntity.ok(BaseResponse.<EmployeeResponseDto>builder()
                .code(200)
                .success(true)
                .message("Personel başarıyla eklendi.")
                .data(responseDto)
                .build());
    }

}
