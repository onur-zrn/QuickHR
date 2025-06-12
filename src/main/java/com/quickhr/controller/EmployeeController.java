package com.quickhr.controller;

import com.quickhr.dto.response.*;
import com.quickhr.service.*;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EMPLOYEE)
@CrossOrigin(origins = "*")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping(EMPLOYEE_DASHBOARD)
    public ResponseEntity<BaseResponse<EmployeeDashboardResponseDto>> getEmployeeDashboard(@RequestParam String token) {
        EmployeeDashboardResponseDto dashboard = employeeService.getEmployeeDashboard(token);
        return ResponseEntity.ok(BaseResponse.<EmployeeDashboardResponseDto>builder()
                .code(200)
                .data(dashboard)
                .success(true)
                .message("Employee dashboard data retrieved!")
                .build());
    }

}
