package com.quickhr.dto.response;

import java.util.*;

public record CompanyDashboardResponseDto(
        String title,
        int totalEmployees,
        int pendingLeaveRequests,
        List<String> upcomingHolidays,
        List<String> recentAnnouncements
) {

}