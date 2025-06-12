package com.quickhr.dto.response;

import java.util.*;

public record AdminDashboardResponseDto(
        String title,
        int totalCompanies,
        int totalEmployees,
        int activeSessions,
        List<String> recentActivities
) {
}