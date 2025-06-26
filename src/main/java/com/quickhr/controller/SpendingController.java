package com.quickhr.controller;

import com.quickhr.dto.request.CreatePersonalSpendingRequestDto;
import com.quickhr.dto.request.ExpenseApproveRejectRequestDto;
import com.quickhr.dto.request.UpdatePersonalSpendingRequestDto;
import com.quickhr.dto.response.BaseResponse;
import com.quickhr.dto.response.PersonalSpendingDetailResponseDto;
import com.quickhr.dto.response.PersonalSpendingSummaryDto;
import com.quickhr.service.SpendingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	//@PostMapping(value = "/api/employee/expenses2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<BaseResponse<PersonalSpendingDetailResponseDto>> createExpense(
//			@RequestHeader String token,
//			@RequestPart @Valid CreatePersonalSpendingRequestDto expenseData,
//			@RequestPart MultipartFile billDocument
//	) {
//		PersonalSpendingDetailResponseDto result = spendingService.createExpense2(expenseData, billDocument, token);
//		return ResponseEntity.ok(BaseResponse.<PersonalSpendingDetailResponseDto>builder()
//				.message("Harcama ve belge başarıyla eklendi.")
//				.code(200)
//				.success(true)
//				.data(result)
//				.build());
//	}

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

	// Onay bekleyen harcamalar (Yönetici) - Pageable
	@GetMapping(PENDING_EXPENSES_MANAGER)
	public ResponseEntity<BaseResponse<Page<PersonalSpendingSummaryDto>>> getPendingExpenses(
			@RequestHeader String token, Pageable pageable) {

		Page<PersonalSpendingSummaryDto> expenses = spendingService.getPendingExpensesForManager(token, pageable);

		return ResponseEntity.ok(BaseResponse.<Page<PersonalSpendingSummaryDto>>builder()
				.message("Onay bekleyen harcamalar listelendi.")
				.code(200)
				.success(true)
				.data(expenses)
				.build());
	}

	// Onaylanmış harcamalar (Yönetici) - Pageable
	@GetMapping(APPROVED_EXPENSES_MANAGER)
	public ResponseEntity<BaseResponse<Page<PersonalSpendingSummaryDto>>> getApprovedExpenses(
			@RequestHeader String token, Pageable pageable) {

		Page<PersonalSpendingSummaryDto> expenses = spendingService.getApprovedExpensesForManager(token, pageable);

		return ResponseEntity.ok(BaseResponse.<Page<PersonalSpendingSummaryDto>>builder()
				.message("Onaylanmış harcamalar listelendi.")
				.code(200)
				.success(true)
				.data(expenses)
				.build());
	}

	// Reddedilmiş harcamalar (Yönetici) - Pageable
	@GetMapping(REJECTED_EXPENSES_MANAGER)
	public ResponseEntity<BaseResponse<Page<PersonalSpendingSummaryDto>>> getRejectedExpenses(
			@RequestHeader String token, Pageable pageable) {

		Page<PersonalSpendingSummaryDto> expenses = spendingService.getRejectedExpensesForManager(token, pageable);

		return ResponseEntity.ok(BaseResponse.<Page<PersonalSpendingSummaryDto>>builder()
				.message("Reddedilmiş harcamalar listelendi.")
				.code(200)
				.success(true)
				.data(expenses)
				.build());
	}




	//Tüm harcamalar reddedilenler hariç
	@GetMapping(EXPENSES_LIST_MANAGER)
	public ResponseEntity<BaseResponse<List<PersonalSpendingSummaryDto>>> getAllExpensesForManager(
			@RequestHeader String token) {

		List<PersonalSpendingSummaryDto> expenses = spendingService.getAllExpensesForManager(token);

		return ResponseEntity.ok(BaseResponse.<List<PersonalSpendingSummaryDto>>builder()
				.message("Personel harcamaları listelendi (Reddedilenler hariç).")
				.code(200)
				.success(true)
				.data(expenses)
				.build());
	}

	@GetMapping(EXPENSE_DETAILS_MANAGER) // GET /api/company/expenses/{id}
	public ResponseEntity<BaseResponse<PersonalSpendingDetailResponseDto>> getExpenseDetailForManager(
			@RequestHeader String token,
			@PathVariable Long id) {

		PersonalSpendingDetailResponseDto detail = spendingService.getExpenseDetailForManager(token, id);

		return ResponseEntity.ok(BaseResponse.<PersonalSpendingDetailResponseDto>builder()
				.message("Harcama detayı getirildi (Yönetici).")
				.code(200)
				.success(true)
				.data(detail)
				.build());
	}

	@PutMapping(APPROVE_REJECT_EXPENSE)
	public ResponseEntity<BaseResponse<Boolean>> approveRejectExpense(
			@RequestHeader String token,
			@RequestBody ExpenseApproveRejectRequestDto dto) {

		Boolean result = spendingService.approveRejectExpense(token, dto);
		String message = result ? "Harcama onaylandı!" : "Harcama reddedildi!";

		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				.code(200)
				.data(result)
				.success(true)
				.message(message)
				.build());
	}

}
