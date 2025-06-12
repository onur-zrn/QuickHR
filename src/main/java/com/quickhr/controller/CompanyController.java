package com.quickhr.controller;

import com.quickhr.dto.response.*;
import com.quickhr.service.*;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

}
