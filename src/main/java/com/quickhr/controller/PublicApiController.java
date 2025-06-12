package com.quickhr.controller;

import com.quickhr.dto.response.*;
import com.quickhr.entity.*;
import com.quickhr.service.*;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PUBLIC_API)
@CrossOrigin(origins = "*")
public class PublicApiController {
	private final PublicApiService publicApiService;

	@GetMapping(HOLIDAYS)
	public ResponseEntity<BaseResponse<List<PublicHolidays>>> findAllPublicHolidays() {
		List<PublicHolidays> holidayList = publicApiService.findAllPublicHolidays();
		return ResponseEntity.ok(BaseResponse.<List<PublicHolidays>>builder()
				.code(200)
				.data(holidayList)
				.success(true)
				.message("All public holidays were successfully brought in!")
				.build());
	}

	@GetMapping(CURRENT_YEAR_HOLIDAYS)
	public ResponseEntity<BaseResponse<List<PublicHolidays>>> findByPublicHolidaysCurrentYear() {
		List<PublicHolidays> holidayList = publicApiService.findByPublicHolidaysCurrentYear();
		return ResponseEntity.ok(BaseResponse.<List<PublicHolidays>>builder()
				.code(200)
				.data(holidayList)
				.success(true)
				.message("The public holidays of the current year have been successfully brought in!")
				.build());
	}

	/**
	 * Public Holidays Up To Last Three Years (Public Holidays In Year Entered)
	 * @param year Requested Year(Ex: /year-holidays?year=2025)
	 */
	@GetMapping(YEAR_HOLIDAYS)
	public ResponseEntity<BaseResponse<List<PublicHolidays>>> searchPublicHolidays(@RequestParam Integer year) {
		List<PublicHolidays> holidayList = publicApiService.findBySearchedPublicHolidays(year);
		String message = String.format("Public holidays of the %d year have been successfully brought in!", year);
		return ResponseEntity.ok(BaseResponse.<List<PublicHolidays>>builder()
				.code(200)
				.data(holidayList)
				.success(true)
				.message(message)
				.build());
	}

	@GetMapping(HOMEPAGE_CONTENT)
	public ResponseEntity<BaseResponse<HomePageContentResponseDto>> getHomePageContent() {
		HomePageContentResponseDto homePageContent = publicApiService.getHomePageContent();
		return ResponseEntity.ok(BaseResponse.<HomePageContentResponseDto>builder()
				.code(200)
				.data(homePageContent)
				.success(true)
				.message("Home page contents have been brought in!")
				.build());
	}

	@GetMapping(PLATFORM_FEATURES)
	public ResponseEntity<BaseResponse<PlatformFeaturesResponseDto>> getPlatformFeatures() {
		PlatformFeaturesResponseDto platformFeatures = publicApiService.getPlatformFeatures();
		return ResponseEntity.ok(BaseResponse.<PlatformFeaturesResponseDto>builder()
				.code(200)
				.data(platformFeatures)
				.success(true)
				.message("Platform features have been created!")
				.build());
	}

	@GetMapping(HOW_IT_WORKS)
	public ResponseEntity<BaseResponse<HowItWorksResponseDto>> getHowItWorks() {
		HowItWorksResponseDto howItWorks = publicApiService.getHowItWorks();
		return ResponseEntity.ok(BaseResponse.<HowItWorksResponseDto>builder()
				.code(200)
				.data(howItWorks)
				.success(true)
				.message("Platform working structure!")
				.build());
	}
}
