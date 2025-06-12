package com.quickhr.repository;

import com.quickhr.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.*;
import java.util.*;

public interface PublicHolidaysRepository extends JpaRepository<PublicHolidays, Long> {
	
	
	@Query("SELECT p FROM PublicHolidays p WHERE p.startDate >= :startDate AND p.endDate <= :endDate")
	List<PublicHolidays> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
