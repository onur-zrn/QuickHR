package com.quickhr.controller;

import com.quickhr.dto.request.CreateLeaveRequestDto;
import com.quickhr.dto.request.EmployeeUpdateProfileRequestDto;
import com.quickhr.dto.response.*;
import com.quickhr.entity.Permission;
import com.quickhr.service.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EMPLOYEE)
@CrossOrigin(origins = "*")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final PermissionService permissionService;

    @GetMapping(EMPLOYEE_DASHBOARD)
    public ResponseEntity<BaseResponse<EmployeeDashboardResponseDto>> getEmployeeDashboard(@RequestHeader String token) {
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
    //İzin bakiyesi ve yıllık izin durumu
    @GetMapping(LEAVES_BALANCE)
    public ResponseEntity<BaseResponse<String>> getLeavesBalance(@RequestHeader String token) {
        return ResponseEntity.ok(BaseResponse.<String>builder()
                .code(200)
                .data(permissionService.getLeavesBalance(token))
                .success(true)
                .message("Employee dashboard data retrieved!")
                .build());
    }

    //İzin talebi detayı
    @GetMapping(LEAVES_DETAIL)
    public ResponseEntity<BaseResponse<Permission>> getLeavesDetail(@RequestHeader String token, @PathVariable Long id) {
        return ResponseEntity.ok(BaseResponse.<Permission>builder()
                .code(200)
                .data(permissionService.getLeavesDetail(token,id))
                .success(true)
                .message("Employee dashboard data retrieved!")
                .build());
    }
    //İzin talebi oluştur
    @PostMapping(CREATE_LEAVE)
    public ResponseEntity<BaseResponse<Boolean>> createWorkHoliday(@RequestHeader String token, @RequestBody @Valid CreateLeaveRequestDto dto) {

        return ResponseEntity.ok(BaseResponse.<Boolean>builder().success(true)
                .message("İzin oluşturuldu")
                .code(200)
                .data(permissionService.createWorkHoliday(token,dto))
                .build());

    }
//    @GetMapping(LEAVES)// - Kendi izin geçmişi (detaylı bilgiler dahil)
//    public ResponseEntity<BaseResponse<Boolean>> createWorkHoliday(@RequestHeader String token, @RequestBody @Valid CreateLeaveRequestDto dto) {
//
//        return ResponseEntity.ok(BaseResponse.<Boolean>builder().success(true)
//                .message("İzin oluşturuldu")
//                .code(200)
//                .data(permissionService.createWorkHoliday(token,dto))
//                .build());
//
//    }

    @GetMapping(ANNUAL_LEAVE_DETAILS)
    public ResponseEntity<BaseResponse<AnnualLeaveDetailsDto>> getLeavesDetail(@RequestHeader String token) {
        return ResponseEntity.ok(BaseResponse.<AnnualLeaveDetailsDto>builder()
                .code(200)
                .data(permissionService.getAnnualLeavesDetail(token))
                .success(true)
                .message("Employee dashboard data retrieved!")
                .build());
    }
}
