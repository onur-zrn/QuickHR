package com.quickhr.controller;

import com.quickhr.dto.request.CreatePersonalSpendingRequestDto;
import com.quickhr.dto.request.UpdatePersonalSpendingRequestDto;
import com.quickhr.dto.response.BaseResponse;
import com.quickhr.dto.response.PersonalSpendingDetailResponseDto;
import com.quickhr.dto.response.PersonalSpendingSummaryDto;
import com.quickhr.dto.response.PersonalSpendingSummaryWithTotalResponseDto;
import com.quickhr.service.SpendingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EMPLOYEE)

@CrossOrigin("*")

public class SpendingControllerEmployee {

	private final SpendingService spendingService;

	@GetMapping(EXPENSES)
	public ResponseEntity<BaseResponse<List<PersonalSpendingSummaryDto>>> getAllExpenses(
			@RequestHeader String token, Pageable pageable) {

		List<PersonalSpendingSummaryDto> expenses = spendingService.getAllExpensesSummaryforPersonal(token, pageable);

		return ResponseEntity.ok(BaseResponse.<List<PersonalSpendingSummaryDto>>builder()
				.message("Harcamalar listelendi")
				.code(200)
				.success(true)
				.data(expenses)
				.build());
	}

	@GetMapping(EXPENSE_DETAIL)
	public ResponseEntity<BaseResponse<PersonalSpendingDetailResponseDto>> getExpenseDetail(
			@RequestHeader String token, @PathVariable Long id) {

		PersonalSpendingDetailResponseDto detail = spendingService.getExpenseDetail(token,id);

		return ResponseEntity.ok(BaseResponse.<PersonalSpendingDetailResponseDto>builder()
				.message("Harcama detayı getirildi")
				.code(200)
				.success(true)
				.data(detail)
				.build());
	}

	@PostMapping(CREATE_EXPENSE)
	public ResponseEntity<BaseResponse<Boolean>> createExpense(
			@RequestHeader String token,
			@RequestBody @Valid CreatePersonalSpendingRequestDto dto
			) {

		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				.message("Harcama başarıyla eklendi")
				.code(200)
				.success(true)
				.data(spendingService.createExpense(token, dto))
				.build());
	}

	@PutMapping(UPDATE_EXPENSE)
	public ResponseEntity<BaseResponse<Boolean>> updateExpense(
			@RequestHeader String token,
			@PathVariable Long id,
			@RequestBody @Valid UpdatePersonalSpendingRequestDto dto) {
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				.message("Harcama başarıyla güncellendi.")
				.code(200)
				.success(true)
				.data(spendingService.updateExpense(token, id, dto))
				.build());
	}

	@DeleteMapping(DELETE_EXPENSE)
	public ResponseEntity<BaseResponse<Boolean>> deleteExpense(
			@RequestHeader String token,
			@PathVariable Long id) {
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				.message("Harcama başarıyla silindi.")
				.code(200)
				.success(true)
				.data(spendingService.deleteExpense(token, id))
				.build());
	}

	@GetMapping(EXPENSES_MONTHLY_SUMMARY) // /api/employee/expenses/monthly-summary?year=2024&month=6
	public ResponseEntity<BaseResponse<PersonalSpendingSummaryWithTotalResponseDto>> getMonthlySummary(
			@RequestHeader String token,
			@RequestParam Integer year,
			@RequestParam Integer month) {

		PersonalSpendingSummaryWithTotalResponseDto result = spendingService.getMonthlySummary(token, year, month);

		return ResponseEntity.ok(BaseResponse.<PersonalSpendingSummaryWithTotalResponseDto>builder()
				.message(year + "/" + month + " aylık harcamalar listelendi.")
				.code(200)
				.success(true)
				.data(result)
				.build());
	}
}
