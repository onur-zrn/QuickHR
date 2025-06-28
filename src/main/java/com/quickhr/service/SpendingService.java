package com.quickhr.service;

import com.quickhr.dto.request.CreatePersonalSpendingRequestDto;
import com.quickhr.dto.request.ExpenseApproveRejectRequestDto;
import com.quickhr.dto.request.UpdatePersonalSpendingRequestDto;
import com.quickhr.dto.response.PersonalSpendingDetailResponseDto;
import com.quickhr.dto.response.PersonalSpendingSummaryDto;
import com.quickhr.dto.response.PersonalSpendingSummaryWithTotalResponseDto;
import com.quickhr.entity.PersonalSpending;
import com.quickhr.entity.User;
import com.quickhr.enums.spendings.ESpendingState;
import com.quickhr.enums.user.EUserRole;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.mapper.PersonalSpendingMapper;
import com.quickhr.repository.PersonalSpendingRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpendingService {

    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final PersonalSpendingRepository personalSpendingRepository;

    public Optional<PersonalSpending> getPersonalSpendingById(Long id) {
        return personalSpendingRepository.findById(id);
    }

    public List<PersonalSpendingSummaryDto> getAllExpensesSummaryforPersonal(String token, Pageable pageable) {
        User user = userService.getUserFromToken(token);

        return personalSpendingRepository
                .findAllByUserIdAndSpendingStateNot(user.getId(), ESpendingState.REJECTED, pageable)
                .stream()
                .map(PersonalSpendingMapper.INSTANCE::toPersonalSpendingSummaryDto)   // Mapper ile dönüşüm
                .toList();
    }

    public PersonalSpendingDetailResponseDto getExpenseDetail(String token, Long id) {
        // Token'dan kullanıcıyı bul
        User user = userService.getUserFromToken(token);


        // PersonalSpending id ile bul
        PersonalSpending personalSpending = getPersonalSpendingById(id)
                .orElseThrow(() -> new HRAppException(ErrorType.EXPENSE_NOT_FOUND));

        if (!personalSpending.getUserId().equals(user.getId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        return PersonalSpendingMapper.INSTANCE.toPersonalSpendingDetailResponseDto(personalSpending);
    }

    public Boolean createExpense(String token, CreatePersonalSpendingRequestDto dto) {
        // Token'dan kullanıcıyı bul
        User user = userService.getUserFromToken(token);

        // Mapper'dan entity'yi üret
        PersonalSpending expense = PersonalSpendingMapper.INSTANCE.toPersonalSpending(dto);
        expense.setUserId(user.getId());
        expense.setSpendingState(ESpendingState.PENDING);

        personalSpendingRepository.save(expense);
        return true;
    }

    public Boolean updateExpense(String token, Long id, UpdatePersonalSpendingRequestDto dto) {
        User user = userService.getUserFromToken(token);
        PersonalSpending expense = personalSpendingRepository.findById(id)
                .orElseThrow(() -> new HRAppException(ErrorType.EXPENSE_NOT_FOUND));

        if (!expense.getUserId().equals(user.getId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        // Eğer durum PENDING değilse silmeye izin verme
        if (expense.getSpendingState() != ESpendingState.PENDING) {
            throw new HRAppException(ErrorType.INVALID_EXPENSE_OPERATION);
        }

        // Null olan alanlar es geçilecek
        PersonalSpendingMapper.INSTANCE.updatePersonalSpendingFromDto(dto, expense);

        personalSpendingRepository.save(expense);
        return true;
    }

    public Boolean deleteExpense(String token, Long id) {
        User user = userService.getUserFromToken(token);
        PersonalSpending expense = personalSpendingRepository.findById(id)
                .orElseThrow(() -> new HRAppException(ErrorType.EXPENSE_NOT_FOUND));

        if (!expense.getUserId().equals(user.getId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        // Eğer durum PENDING değilse silmeye izin verme
        if (expense.getSpendingState() != ESpendingState.PENDING) {
            throw new HRAppException(ErrorType.INVALID_EXPENSE_OPERATION);
        }

        personalSpendingRepository.delete(expense);
        return true;
    }

    // Onay bekleyen harcamalar (Yönetici) - Pageable
    public Page<PersonalSpendingSummaryDto> getPendingExpensesForManager(String token, Pageable pageable) {
        User user = userService.getUserFromToken(token);

        if (user.getRole() != EUserRole.MANAGER) {
            throw new HRAppException(ErrorType.USER_NOT_MANAGER);
        }

        return personalSpendingRepository
                .findAllBySpendingStateAndCompanyId(ESpendingState.PENDING, user.getCompanyId(), pageable)
                .map(PersonalSpendingMapper.INSTANCE::toPersonalSpendingSummaryDto);
    }

    // Onaylanmış harcamalar (Yönetici) - Pageable
    public Page<PersonalSpendingSummaryDto> getApprovedExpensesForManager(String token, Pageable pageable) {
        User user = userService.getUserFromToken(token);

        if (user.getRole() != EUserRole.MANAGER) {
            throw new HRAppException(ErrorType.USER_NOT_MANAGER);
        }

        if (user.getCompanyId() == null) {
            throw new HRAppException(ErrorType.COMPANY_OR_EMPLOYEE_NOT_FOUND);
        }

        return personalSpendingRepository
                .findAllBySpendingStateAndCompanyId(ESpendingState.APPROVED, user.getCompanyId(), pageable)
                .map(PersonalSpendingMapper.INSTANCE::toPersonalSpendingSummaryDto);
    }

    // Reddedilmiş harcamalar (Yönetici) - Pageable
    public Page<PersonalSpendingSummaryDto> getRejectedExpensesForManager(String token, Pageable pageable) {
        User user = userService.getUserFromToken(token);

        if (user.getRole() != EUserRole.MANAGER) {
            throw new HRAppException(ErrorType.USER_NOT_MANAGER);
        }

        return personalSpendingRepository
                .findAllBySpendingStateAndCompanyId(ESpendingState.REJECTED, user.getCompanyId(), pageable)
                .map(PersonalSpendingMapper.INSTANCE::toPersonalSpendingSummaryDto);
    }

    public Page<PersonalSpendingSummaryDto> getAllExpensesForManager(String token, Pageable pageable) {
        User user = userService.getUserFromToken(token);

        if (user.getRole() != EUserRole.MANAGER) {
            throw new HRAppException(ErrorType.USER_NOT_MANAGER);
        }

        return personalSpendingRepository
                .findAllBySpendingStateAndCompanyId(user.getCompanyId(), ESpendingState.REJECTED, pageable) // REJECTED hariç listelemek
                .map(PersonalSpendingMapper.INSTANCE::toPersonalSpendingSummaryDto);
    }

    public PersonalSpendingDetailResponseDto getExpenseDetailForManager(String token, Long id) {
        User manager = userService.getUserFromToken(token);

        if (manager.getRole() != EUserRole.MANAGER) {
            throw new HRAppException(ErrorType.USER_NOT_MANAGER);
        }

        PersonalSpending expense = personalSpendingRepository.findById(id)
                .orElseThrow(() -> new HRAppException(ErrorType.EXPENSE_NOT_FOUND));

        // Harcama sahibini bul, Optional ile kontrol
        User expenseOwner = userService.findUserById(expense.getUserId())
                .orElseThrow(() -> new HRAppException(ErrorType.COMPANY_OR_EMPLOYEE_NOT_FOUND));

        if (!expenseOwner.getCompanyId().equals(manager.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        return PersonalSpendingMapper.INSTANCE.toPersonalSpendingDetailResponseDto(expense);
    }

    public Boolean approveRejectExpense(String token, ExpenseApproveRejectRequestDto dto) {
        User manager = userService.getUserFromToken(token);

        if (manager.getRole() != EUserRole.MANAGER) {
            throw new HRAppException(ErrorType.USER_NOT_MANAGER);
        }

        PersonalSpending expense = personalSpendingRepository.findById(dto.id())
                .orElseThrow(() -> new HRAppException(ErrorType.EXPENSE_NOT_FOUND));

        User expenseOwner = userService.findUserById(expense.getUserId())
                .orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_FOUND));

        if ((!Objects.equals(manager.getCompanyId(), expenseOwner.getCompanyId()))) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        if (expense.getSpendingState() != ESpendingState.PENDING) {
            throw new HRAppException(ErrorType.INVALID_EXPENSE_OPERATION);
        }

        expense.setSpendingState(dto.isApproved() ? ESpendingState.APPROVED : ESpendingState.REJECTED);

        personalSpendingRepository.save(expense);

        return dto.isApproved();
    }

    public PersonalSpendingSummaryWithTotalResponseDto getMonthlySummary(String token, Integer year, Integer month) {
        Long userId = userService.getUserFromToken(token).getId();

        // Kullanıcının belirtilen ay ve yıl için onaylanmış harcamaları getir
        List<PersonalSpending> spendings = personalSpendingRepository.findAllByUserIdAndSpendingState(userId, ESpendingState.APPROVED)
                .stream()
                .filter(ps -> ps.getSpendingDate().getYear() == year && ps.getSpendingDate().getMonthValue() == month)
                .toList();

        List<PersonalSpendingSummaryDto> summaries = spendings.stream()
                .map(PersonalSpendingMapper.INSTANCE::toPersonalSpendingSummaryDto)
                .collect(Collectors.toList());

        Double totalAmount = spendings.stream()
                .mapToDouble(PersonalSpending::getBillAmount)
                .sum();

        return new PersonalSpendingSummaryWithTotalResponseDto(summaries, totalAmount);
    }

    public PersonalSpendingSummaryWithTotalResponseDto getCompanyUserMonthlySummary(String token, Long userId, Integer year, Integer month) {
        User manager = userService.getUserFromToken(token);

        if (manager.getRole() != EUserRole.MANAGER) {
            throw new HRAppException(ErrorType.USER_NOT_MANAGER);
        }

        Optional<User> user = userService.findUserById(userId);
        if (user.isEmpty()) {
            throw new HRAppException(ErrorType.USER_NOT_FOUND);
        }

        if (!Objects.equals(manager.getCompanyId(), user.get().getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }


        List<PersonalSpending> expenses = personalSpendingRepository.findAllByUserIdAndYearAndMonthAndSpendingState(
                userId,
                year,
                month,
                ESpendingState.APPROVED
        );

        List<PersonalSpendingSummaryDto> summaries = expenses.stream()
                .map(PersonalSpendingMapper.INSTANCE::toPersonalSpendingSummaryDto)
                .toList();

        Double totalAmount = expenses.stream()
                .mapToDouble(PersonalSpending::getBillAmount)
                .sum();

        return new PersonalSpendingSummaryWithTotalResponseDto(summaries, totalAmount);
    }
}
