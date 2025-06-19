package com.quickhr.controller;

import com.quickhr.dto.request.EmployeeRequestDto;
import com.quickhr.dto.request.EmployeeUpdateRequestDto;
import com.quickhr.dto.request.isApprovedRequestLeaveRequestDto;
import com.quickhr.dto.response.*;
import com.quickhr.entity.Permission;
import com.quickhr.enums.user.EUserState;
import com.quickhr.service.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.List;

import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(COMPANY)
@CrossOrigin(origins = "*")
public class CompanyController {
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final PermissionService permissionService;

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
    public ResponseEntity<BaseResponse<EmployeeDetailsResponseDto>> getEmployeeDetailsById(@RequestParam String token, @PathVariable Long id) {
        EmployeeDetailsResponseDto employeeDetails = companyService.getEmployeeDetailsById(token, id);
        return ResponseEntity.ok(BaseResponse.<EmployeeDetailsResponseDto>builder()
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

    @PutMapping(MAKE_PERSONAL_STATUS_IN_ACTIVE)
    public ResponseEntity<BaseResponse<Boolean>> makeActivePersonal(@RequestParam String token, @RequestParam Long id) {
        Boolean result = companyService.makeActivePersonal(token, id);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .data(result)
                .success(true)
                .message("Active personal data retrieved!")
                .build());
    }

    @PutMapping(MAKE_PERSONAL_STATUS_IN_PASSIVE)
    public ResponseEntity<BaseResponse<Boolean>> makePassivePersonal(@RequestParam String token, @RequestParam Long id) {
        Boolean result = companyService.makePassivePersonal(token, id);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .data(result)
                .success(true)
                .message("Passive personal data retrieved!")
                .build());
    }

    @PutMapping(CHANGE_PERSONAL_STATUS)
    public ResponseEntity<BaseResponse<Boolean>> changePersonalStatus(
            @RequestParam String token, @RequestParam Long id, @RequestParam EUserState newUserState) {
        Boolean result = companyService.changePersonalStatus(token, id, newUserState);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .data(result)
                .success(true)
                .message("Personal status updated!")
                .build());
    }

    @GetMapping(ACTIVE_PERSONAL)
    public ResponseEntity<BaseResponse<List<PersonalStateResponseDto>>> getActivePersonal(@RequestParam String token) {
        return ResponseEntity.ok(BaseResponse.<List<PersonalStateResponseDto>>builder()
                .code(200)
                .data(companyService.getActivePersonal(token))
                .success(true)
                .message("Active personals retrieved!")
                .build());
    }

    @GetMapping(PASSIVE_PERSONAL)
    public ResponseEntity<BaseResponse<List<PersonalStateResponseDto>>> getPassivePersonal(@RequestParam String token) {
        return ResponseEntity.ok(BaseResponse.<List<PersonalStateResponseDto>>builder()
                .code(200)
                .data(companyService.getPassivePersonal(token))
                .success(true)
                .message("Passive personals retrieved!")
                .build());
    }

    @GetMapping(PENDING_PERSONAL)
    public ResponseEntity<BaseResponse<List<PersonalStateResponseDto>>> getPendingPersonal(@RequestParam String token) {
        return ResponseEntity.ok(BaseResponse.<List<PersonalStateResponseDto>>builder()
                .code(200)
                .data(companyService.getPendingPersonal(token))
                .success(true)
                .message("Pending personals retrieved!")
                .build());
    }

    @GetMapping(DELETED_PERSONAL)
    public ResponseEntity<BaseResponse<List<PersonalStateResponseDto>>> getDeletedPersonal(@RequestParam String token) {
        return ResponseEntity.ok(BaseResponse.<List<PersonalStateResponseDto>>builder()
                .code(200)
                .data(companyService.getDeletedPersonal(token))
                .success(true)
                .message("Deleted personals retrieved!")
                .build());
    }

    @DeleteMapping(MAKE_DELETED_PERSONAL)
    public ResponseEntity<BaseResponse<Boolean>> makeDeletedPersonal(@RequestParam String token, @PathVariable Long id) {
        Boolean result = companyService.makeDeletedPersonal(token, id);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .data(result)
                .success(true)
                .message("Personal soft delete successfully!")
                .build());
    }

    @GetMapping(REQUEST_LEAVE)
    public ResponseEntity<BaseResponse<List<Permission>>> requestLeave(@RequestParam String token) {
        return ResponseEntity.ok(BaseResponse.<List<Permission>>builder()
                .code(200)
                .data(permissionService.pendingLeave(token))
                .success(true)
                .message("Request Leave brought in!")
                .build());
    }

    @GetMapping(APPROVED_LEAVE)
    public ResponseEntity<BaseResponse<List<Permission>>> approvedLeave(@RequestParam String token) {
        return ResponseEntity.ok(BaseResponse.<List<Permission>>builder()
                .code(200)
                .data(permissionService.approvedLeave(token))
                .success(true)
                .message("Approved Leave brought in!")
                .build());
    }

    @PutMapping(IS_APPROVED_REQUEST_LEAVE)
    public ResponseEntity<BaseResponse<Boolean>> isApprovedRequestLeave (@RequestParam String token,
                                                                         isApprovedRequestLeaveRequestDto dto) {
        Boolean result = permissionService.isApprovedRequestLeave(token, dto);
        String message;
        if (result) {
            message = "RequestLeave Approved!";
        } else {
            message = "RequestLeave Rejected!";
        }
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .data(result)
                .success(true)
                .message(message)
                .build());
    }
}
