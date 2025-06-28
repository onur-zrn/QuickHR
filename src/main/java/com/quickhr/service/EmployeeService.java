package com.quickhr.service;

import com.quickhr.dto.request.EmployeeUpdateProfileRequestDto;
import com.quickhr.dto.request.EmployeeUpdateRequestDto;
import com.quickhr.dto.response.*;
import com.quickhr.entity.Employee;
import com.quickhr.entity.Permission;
import com.quickhr.entity.User;
import com.quickhr.enums.permissions.EPermissionPolicy;
import com.quickhr.enums.permissions.EPermissionState;
import com.quickhr.enums.permissions.EPermissionType;
import com.quickhr.enums.user.EUserRole;
import com.quickhr.enums.user.EUserState;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.mapper.EmployeeMapper;
import com.quickhr.mapper.UserMapper;
import com.quickhr.repository.EmployeeRepository;
import com.quickhr.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private final PublicHolidayService publicHolidayService;
    private final SpendingService spendingService;
    private final EmbezzlementService embezzlementService;
    private final PermissionRepository permissionRepository;

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Long countPersonal() {
        return employeeRepository.countPersonal();
    }

    public Long countActivePersonal() {
        return employeeRepository.countActivePersonal();
    }

    public Long countActivePersonalByCompanyId(Long companyId) {
        return employeeRepository.countActivePersonalByCompanyId(companyId);
    }

    public Long countApprovedPermissionsToday(Long company_id) {
        return employeeRepository.countApprovedPermissionsTodayByCompanyId(company_id);
    }


    public Optional<Employee> getEmployeeByUserId(Long id) {
        return employeeRepository.findByUserId(id);
    }

    public EmployeeDashboardResponseDto getEmployeeDashboard(String token) {
        Long userId = userService.getUserFromToken(token).getId();

        // Public Holidays
        List<PublicHolidayResponseDto> holidayDtos = publicHolidayService.findAll();

        Employee employee = getEmployeeByUserId(userId)
                .orElseThrow(() -> new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND));

        // Annual Leave Details
        AnnualLeaveDetailsDto annualLeaveDetailsDto = getAnnualLeaveDetailsDto(employee);

        // Monthly Spending Summary
        PersonalSpendingSummaryWithTotalResponseDto spendingSummary =
                spendingService.getMonthlySummary(token, LocalDate.now().getYear(),LocalDate.now().getMonthValue());

        List<EmbezzlementProductDetailResponseDto> embezzlementProducts = embezzlementService.getMyEmbezzlements(token);

        return EmployeeDashboardResponseDto.of(
                holidayDtos,
                annualLeaveDetailsDto,
                spendingSummary,
                embezzlementProducts
        );
    }

    private AnnualLeaveDetailsDto getAnnualLeaveDetailsDto(Employee employee) {
        int totalLeaves = EPermissionPolicy.getAnnualLeaveDays(employee.getDateOfEmployment());

        // Bu yılın başı ve sonu
        LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
        LocalDate endOfYear = LocalDate.now().withMonth(12).withDayOfMonth(31);

        List<Permission> usedLeaves = permissionRepository.findAllByUserIdAndPermissionTypeAndPermissionStateAndBeginDateBetween(
                employee.getUserId(),
                EPermissionType.ANNUAL_LEAVE,
                EPermissionState.APPROVED,
                startOfYear,
                endOfYear
        );

        // Gün toplamı
        int used_leaves =  usedLeaves.stream()
                .mapToInt(p -> (int) (p.getEndDate().toEpochDay() - p.getBeginDate().toEpochDay() + 1)) // her iki gün dahil
                .sum();
        int remainingLeaves = totalLeaves - used_leaves;

        return new AnnualLeaveDetailsDto(totalLeaves, used_leaves, remainingLeaves);
    }
    public Page<EmployeeResponseDto> getEmployeeInCompany(List<Long> userIds, Pageable pageable) {
        Page<Employee> employees = employeeRepository.findByUserIdIn(userIds, pageable);
        return employees.map(EmployeeMapper.INSTANCE::toDto);
    }

    public Optional<Employee> getEmployeeInCompany(Long userId) {
        if (userId == null) {
            throw new HRAppException(ErrorType.USER_NOT_FOUND);
        }
        return employeeRepository.findByUserId(userId);
    }

    @Transactional
    public EmployeeResponseDto updatePersonalDetails(String token, Long id, EmployeeUpdateRequestDto dto) {
        User manager = userService.getUserFromToken(token);

        if(!manager.getRole().equals(EUserRole.MANAGER)){
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND));

        if(!manager.getCompanyId().equals(employee.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        EmployeeMapper.INSTANCE.updateEmployeeFromDto(dto, employee);

        Employee updated = employeeRepository.save(employee);

        // Employee ile ilişkili User'ı bul
        Optional<User> userOpt = userService.findUserById(employee.getUserId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Employee'deki ortak alanları User'a kopyala
            UserMapper.INSTANCE.updateUserFromEmployee(employee, user);
            userService.save(user);
        }

        return EmployeeMapper.INSTANCE.toDto(updated);
    }

    @Transactional
    public EmployeeResponseDto updateProfile(String token, EmployeeUpdateProfileRequestDto dto) {
        User employee_user = userService.getUserFromToken(token);


        Employee employee = employeeRepository.findById(employee_user.getId())
                .orElseThrow(() -> new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND));


        EmployeeMapper.INSTANCE.updateEmployeeFromUpdateProfileDto(dto, employee);

        Employee updated = employeeRepository.save(employee);

        // Employee ile ilişkili User'ı bul
        Optional<User> userOpt = userService.findUserById(employee.getUserId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Employee'deki ortak alanları User'a kopyala
            UserMapper.INSTANCE.updateUserFromEmployee(employee, user);
            userService.save(user);
        }

        return EmployeeMapper.INSTANCE.toDto(updated);
    }

    public List<Employee> getActiveUsersByCompany(Long companyId, EUserState userState) {
        return employeeRepository.findAllByCompanyIdAndUserState(companyId, userState);
    }

    public List<Employee> getPassiveUsersByCompany(Long companyId, EUserState userState) {
        return employeeRepository.findAllByCompanyIdAndUserState(companyId, userState);
    }

    public List<Employee> getPendingUsersByCompany(Long companyId, EUserState userState) {
        return employeeRepository.findAllByCompanyIdAndUserState(companyId, userState);
    }

    public List<Employee> getDeletedUsersByCompany(Long companyId, EUserState userState) {
        return employeeRepository.findAllByCompanyIdAndUserState(companyId, userState);
    }

}


