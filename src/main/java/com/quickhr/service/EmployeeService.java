package com.quickhr.service;

import com.quickhr.dto.response.*;
import lombok.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final UserService userService;
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

}
