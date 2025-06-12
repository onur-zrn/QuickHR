package com.quickhr.controller;

import com.quickhr.dto.request.*;
import com.quickhr.dto.response.*;
import com.quickhr.enums.company.ECompanyState;
import com.quickhr.service.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ADMIN)
@CrossOrigin(origins = "*")
public class AdminController {
    private final AdminService adminService;

    @GetMapping(ADMIN_DASHBOARD)
    public ResponseEntity<BaseResponse<AdminDashboardResponseDto>> getAdminDashboard(@RequestParam String token) {
        AdminDashboardResponseDto dashboard = adminService.getAdminDashboard(token);
        return ResponseEntity.ok(BaseResponse.<AdminDashboardResponseDto>builder()
                .code(200)
                .data(dashboard)
                .success(true)
                .message("Admin dashboard data retrieved!")
                .build());
    }

    @PostMapping(ADMIN_LOGIN)
    public ResponseEntity<BaseResponse<String>> doLogin(@RequestBody @Valid AdminLoginRequestDto dto) {
        AdminLoginResponseDto responseDto = adminService.login(dto);
        return ResponseEntity.ok(BaseResponse.<String>builder()
                .code(200)
                .data(String.valueOf(responseDto))
                .success(true)
                .message("Admin Login Successful!")
                .build());
    }

    @GetMapping(PENDING_COMPANY)
    public ResponseEntity<BaseResponse<String>> pendingCompanies(@RequestParam String token) {
        List<CompanyStateResponseDto> pendingCompanies = adminService.listAllPendingCompanies(token);
        return ResponseEntity.ok(BaseResponse.<String>builder()
                .code(200)
                .data(String.valueOf(pendingCompanies))
                .success(true)
                .message("Pending Companies!")
                .build());
    }

    @GetMapping(ACCEPTED_COMPANY)
    public ResponseEntity<BaseResponse<String>> acceptedCompanies(@RequestParam String token) {
        List<CompanyStateResponseDto> acceptedCompanies = adminService.listAllAcceptedCompanies(token);
        return ResponseEntity.ok(BaseResponse.<String>builder()
                .code(200)
                .data(String.valueOf(acceptedCompanies))
                .success(true)
                .message("Accepted Companies!")
                .build());
    }


    @PutMapping(CHANGE_COMPANY_STATUS)
    public ResponseEntity<BaseResponse<Boolean>> changeCompanyStatus(@RequestParam String token,
                                                                     ChangeCompanyStatusRequestDto dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .data(adminService.changeCompanyStatus(token, dto))
                .success(true)
                .message("Change Company Status! - NewCompanyState")
                .build());
    }


    @PostMapping(ADMIN_DEACTIVATE)
    public ResponseEntity<BaseResponse<Boolean>> deactivateAccount(@RequestParam String token) {
        adminService.deActivateAccount(token);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(true)
                .message("Hesap askıya alındı.")
                .data(true)
                .build());
    }
}
