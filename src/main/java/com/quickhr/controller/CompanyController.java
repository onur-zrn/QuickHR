package com.quickhr.controller;

import com.quickhr.dto.request.EmployeeRequestDto;
import com.quickhr.dto.request.EmployeeUpdateRequestDto;
import com.quickhr.dto.response.*;
import com.quickhr.service.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(COMPANY)
@CrossOrigin(origins = "*")
public class CompanyController {
    private final CompanyService companyService;
    private final EmployeeService employeeService;

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
    public ResponseEntity<BaseResponse<Page<EmployeeResponseDto>>> getEmployeeInCompany(
            @RequestParam String token,
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC) Pageable pageable) {

        Page<EmployeeResponseDto> personals = companyService.getEmployeeInCompany(token, pageable);

        if (personals == null || personals.isEmpty()) {
            return ResponseEntity.ok(BaseResponse.<Page<EmployeeResponseDto>>builder()
                    .code(204)
                    .data(Page.empty())
                    .success(true)
                    .message("No personals found for the company.")
                    .build());
        }

        return ResponseEntity.ok(BaseResponse.<Page<EmployeeResponseDto>>builder()
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
    @PutMapping(UPDATE_PERSONAL)
    public ResponseEntity<BaseResponse<EmployeeResponseDto>> updateEmployeePersonalDetails(
            @RequestHeader String token,
            @PathVariable Long id,
            @RequestBody @Valid EmployeeUpdateRequestDto dto) {

        EmployeeResponseDto updatedEmployee = employeeService.updatePersonalDetails(token, id, dto);

        return ResponseEntity.ok(BaseResponse.<EmployeeResponseDto>builder()
                .code(200)
                .success(true)
                .message("Employee personal details updated successfully.")
                .data(updatedEmployee)
                .build());
    }

}
