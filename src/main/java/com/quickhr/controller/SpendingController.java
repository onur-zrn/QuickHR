package com.quickhr.controller;

import com.quickhr.dto.response.BaseResponse;
import com.quickhr.dto.response.PersonalSpendingSummaryDto;
import com.quickhr.service.SpendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")

public class SpendingController {

	private final SpendingService spendingService;

	@GetMapping(EXPENSES)
	public ResponseEntity<BaseResponse<List<PersonalSpendingSummaryDto>>> getAllExpenses(
			@RequestHeader String token) {

		List<PersonalSpendingSummaryDto> expenses = spendingService.getAllExpensesSummaryforPersonal(token);

		return ResponseEntity.ok(BaseResponse.<List<PersonalSpendingSummaryDto>>builder()
				.message("Harcamalar listelendi")
				.code(200)
				.success(true)
				.data(expenses)
				.build());
	}

}
