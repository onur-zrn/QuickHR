package com.quickhr.service;

import com.quickhr.dto.response.PersonalSpendingSummaryDto;
import com.quickhr.entity.User;
import com.quickhr.enums.spendings.ESpendingState;
import com.quickhr.mapper.PersonalSpendingMapper;
import com.quickhr.repository.PersonalSpendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpendingService {

    private final UserService userService;
    private final PersonalSpendingRepository personalSpendingRepository;

    public List<PersonalSpendingSummaryDto> getAllExpensesSummaryforPersonal(String token) {
        User user = userService.getUserFromToken(token);

        return personalSpendingRepository
                .findAllByUserIdAndSpendingStateNot(user.getId(), ESpendingState.REJECTED)
                .stream()
                .map(PersonalSpendingMapper.INSTANCE::toPersonalSpendingSummaryDto)   // Mapper ile dönüşüm
                .toList();
    }
}
