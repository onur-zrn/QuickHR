package com.quickhr.service;

import com.quickhr.dto.request.EmployeeUpdateProfileRequestDto;
import com.quickhr.dto.request.EmployeeUpdateRequestDto;
import com.quickhr.dto.response.*;
import com.quickhr.entity.Employee;
import com.quickhr.entity.User;
import com.quickhr.enums.user.EUserRole;
import com.quickhr.enums.user.EUserState;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.mapper.EmployeeMapper;
import com.quickhr.mapper.UserMapper;
import com.quickhr.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final UserService userService;

    private final EmployeeRepository employeeRepository;

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

    public Long countApprovedPermissionsToday() {
        return employeeRepository.countApprovedPermissionsToday();
    }


    public Optional<Employee> getEmployeeByUserId(Long id) {
        return employeeRepository.findByUserId(id);
    }

    public EmployeeDashboardResponseDto getEmployeeDashboard(String token) {
        userService.getUserFromToken(token);
        return new EmployeeDashboardResponseDto(
                "Employee Dashboard",
                List.of(
                        "Summer Break (Jul 15-20)",
                        "Company Anniversary (Sep 10)"
                ),
                12,
                List.of(
                        "Office party this Friday",
                        "New training program available"
                ),
                List.of(
                        "Complete quarterly self-assessment",
                        "Submit timesheet by Friday"
                )
        );
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


