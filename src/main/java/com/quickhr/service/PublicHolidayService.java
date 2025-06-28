package com.quickhr.service;

import com.quickhr.dto.response.CompanyDashboardResponseDto;
import com.quickhr.dto.response.PublicHolidayResponseDto;
import com.quickhr.entity.User;
import com.quickhr.repository.PublicHolidaysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicHolidayService {

    private final PublicHolidaysRepository publicHolidaysRepository;
    private final UserService userService;

    public List<PublicHolidayResponseDto> findAll() {
        return publicHolidaysRepository.findAll().stream()
                .map(PublicHolidayResponseDto::fromEntity)
                .toList();
    }



}
