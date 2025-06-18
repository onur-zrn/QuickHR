package com.quickhr.controller;

import com.quickhr.dto.request.EmployeeUpdateProfileRequestDto;
import com.quickhr.dto.request.EmployeeUpdateRequestDto;
import com.quickhr.dto.response.*;
import com.quickhr.service.*;
import jakarta.validation.Valid;
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
    @PutMapping(UPDATE_PERSONAL_PROFILE)
    public ResponseEntity<BaseResponse<EmployeeResponseDto>> updateEmployeePersonalDetails(
            @RequestHeader String token,
            @RequestBody @Valid EmployeeUpdateProfileRequestDto dto) {

        EmployeeResponseDto updatedEmployee = employeeService.updateProfile(token, dto);

        return ResponseEntity.ok(BaseResponse.<EmployeeResponseDto>builder()
                .code(200)
                .success(true)
                .message("Employee personal details updated successfully.")
                .data(updatedEmployee)
                .build());
    }

    @GetMapping(ANNUAL_LEAVE_DETAILS)
    public ResponseEntity<BaseResponse<String>> getEmployeeDashboard(@RequestParam String token) {
        return ResponseEntity.ok(BaseResponse.<EmployeeDashboardResponseDto>builder()
                .code(200)
                .data(employeeService.getAnnualLeavesDetail(token))
                .success(true)
                .message("Employee dashboard data retrieved!")
                .build());
    }
}
