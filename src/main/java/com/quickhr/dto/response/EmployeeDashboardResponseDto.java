package com.quickhr.dto.response;

import java.util.*;

public record EmployeeDashboardResponseDto(
        String title,
        List<String> upcomingHolidays,
        Integer leaveBalance,
        List<String> recentAnnouncements,
        List<String> pendingTasks
) {

}
