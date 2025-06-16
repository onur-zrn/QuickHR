package com.quickhr.service;

import com.quickhr.dto.response.*;
import com.quickhr.entity.Employee;
import com.quickhr.entity.User;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.mapper.EmployeeMapper;
import com.quickhr.repository.EmployeeRepository;
import lombok.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final UserService userService;
    private final EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
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
    public List<EmployeeResponseDto> getEmployeeInCompany(List<Long> userIdList) {
        if (userIdList == null || userIdList.isEmpty()) {
            throw new HRAppException(ErrorType.USER_NOT_FOUND);
        }
        System.out.println(userIdList);
        List<Employee> employees = employeeRepository.findByUserIdIn(userIdList);

        System.out.println(employees);
        // DTO listesine dönüştür
        return EmployeeMapper.INSTANCE.toDtoList(employees);
    }

    public Employee getEmployeeInCompany(Long userId) {
        if (userId == null) {
            throw new HRAppException(ErrorType.USER_NOT_FOUND);
        }
        return employeeRepository.findByUserId(userId);
    }
}
