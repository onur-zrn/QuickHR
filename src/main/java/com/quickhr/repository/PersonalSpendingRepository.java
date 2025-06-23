package com.quickhr.repository;

import com.quickhr.entity.PersonalSpending;
import com.quickhr.enums.spendings.ESpendingState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalSpendingRepository extends JpaRepository<PersonalSpending, Long> {

    List<PersonalSpending> findAllByUserIdAndSpendingStateNot(Long userId, ESpendingState spendingState);

}
