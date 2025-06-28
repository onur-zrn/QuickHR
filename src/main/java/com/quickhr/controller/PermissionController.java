package com.quickhr.controller;

import com.quickhr.dto.request.CreateLeaveRequestDto;
import com.quickhr.dto.request.isApprovedRequestLeaveRequestDto;
import com.quickhr.dto.response.AnnualLeaveDetailsDto;
import com.quickhr.dto.response.BaseResponse;
import com.quickhr.entity.Permission;
import com.quickhr.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PermissionController {

    private final PermissionService permissionService;

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
                .message("İzin talebi oluşturuldu")
                .code(200)
                .data(permissionService.createWorkHoliday(token,dto))
                .build());

    }

    @GetMapping(LEAVES)
    public ResponseEntity<BaseResponse<List<Permission>>> getAllMyLeaves(@RequestHeader String token) {
        List<Permission> permissions = permissionService.getAllMyLeaves(token);

        return ResponseEntity.ok(BaseResponse.<List<Permission>>builder()
                .code(200)
                .success(true)
                .message("İzin geçmişi başarıyla getirildi.")
                .data(permissions)
                .build());
    }

    @GetMapping(ANNUAL_LEAVE_DETAILS)
    public ResponseEntity<BaseResponse<AnnualLeaveDetailsDto>> getLeavesDetail(@RequestHeader String token) {
        return ResponseEntity.ok(BaseResponse.<AnnualLeaveDetailsDto>builder()
                .code(200)
                .data(permissionService.getAnnualLeavesDetail(token))
                .success(true)
                .message("Employee dashboard data retrieved!")
                .build());
    }


    @GetMapping(REQUEST_LEAVE)
    public ResponseEntity<BaseResponse<List<Permission>>> requestLeave(@RequestHeader String token) {
        return ResponseEntity.ok(BaseResponse.<List<Permission>>builder()
                .code(200)
                .data(permissionService.pendingLeave(token))
                .success(true)
                .message("Request Leave brought in!")
                .build());
    }

    @GetMapping(APPROVED_LEAVE)
    public ResponseEntity<BaseResponse<List<Permission>>> approvedLeave(@RequestHeader String token) {
        return ResponseEntity.ok(BaseResponse.<List<Permission>>builder()
                .code(200)
                .data(permissionService.approvedLeave(token))
                .success(true)
                .message("Approved Leave brought in!")
                .build());
    }

    @PutMapping(IS_APPROVED_REQUEST_LEAVE)
    public ResponseEntity<BaseResponse<Boolean>> isApprovedRequestLeave (@RequestHeader String token,
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
