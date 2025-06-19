package com.quickhr.service;

import com.quickhr.dto.response.*;
import com.quickhr.entity.*;
import com.quickhr.exception.*;
import com.quickhr.init.PublicApiInitializer;
import com.quickhr.repository.*;
import lombok.*;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PublicApiService {
	private final PublicHolidaysRepository publicHolidaysRepository;

	// All Public Holidays
	public List<PublicHolidays> findAllPublicHolidays() {
		return publicHolidaysRepository.findAll();
	}

	// Current Year Public Holidays
	public List<PublicHolidays> findByPublicHolidaysCurrentYear() {
		LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
		LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12, 31);
		return publicHolidaysRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate);
	}

	// Last Three Years Public Holidays
	public List<PublicHolidays> findBySearchedPublicHolidays(Integer year) {
		if (year <= LocalDate.now().getYear() - 3) {
			throw new HRAppException(ErrorType.IN_LAST_THERE_YEARS);
		}

		if (year > LocalDate.now().getYear()) {
			throw new HRAppException(ErrorType.FUTURE_YEARS);
		}
		LocalDate startDate = LocalDate.of(year, 1, 1);
		LocalDate endDate = LocalDate.of(year, 12, 31);

		return publicHolidaysRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate);
	}

	// Home Page Content
	public HomePageContentResponseDto getHomePageContent() {
		return PublicApiInitializer.homePageContent();
	}

	// Platform Features
	public PlatformFeaturesResponseDto getPlatformFeatures() {
		return PublicApiInitializer.platformFeatures();
	}

	// How It Works
	public HowItWorksResponseDto getHowItWorks() {
		return PublicApiInitializer.howItWorks();
	}

}
