package com.quickhr.repository;

import com.quickhr.entity.PersonalSpending;
import com.quickhr.enums.spendings.ESpendingState;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonalSpendingRepository extends JpaRepository<PersonalSpending, Long> {

    List<PersonalSpending> findAllByUserIdAndSpendingStateNot(Long userId, ESpendingState spendingState, Pageable pageable);

    // Şirket bazlı, duruma göre harcamaları getir (PENDING, APPROVED, REJECTED gibi)
    @Query("""
    SELECT ps FROM PersonalSpending ps
    JOIN User u ON ps.userId = u.id
    WHERE ps.spendingState = :state
    AND u.companyId = :companyId
""")
    Page<PersonalSpending> findAllBySpendingStateAndCompanyId(
            @Param("state") ESpendingState state,
            @Param("companyId") Long companyId,
            Pageable pageable
    );

    // REJECTED haricindeki tüm harcamalar
    @Query("""
    SELECT ps FROM PersonalSpending ps 
    JOIN User u ON ps.userId = u.id 
    WHERE u.companyId = :companyId 
      AND ps.spendingState <> :excludedState
""")
    Page<PersonalSpending> findAllBySpendingStateAndCompanyId(
            @Param("companyId") Long companyId,
            @Param("excludedState") ESpendingState excludedState,
            Pageable pageable
    );

    List<PersonalSpending> findAllByUserIdAndSpendingState(Long userId, ESpendingState spendingState);


    @Query("""
        SELECT ps FROM PersonalSpending ps 
        WHERE ps.userId = :userId
        AND EXTRACT(YEAR FROM ps.spendingDate) = :year
        AND EXTRACT(MONTH FROM ps.spendingDate) = :month
        AND ps.spendingState = :spendingState
    """)
    List<PersonalSpending> findAllByUserIdAndYearAndMonthAndSpendingState(
            @Param("userId") Long userId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("spendingState") ESpendingState spendingState
    );
}
