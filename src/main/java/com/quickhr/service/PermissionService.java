package com.quickhr.service;

import com.quickhr.dto.request.CreateLeaveRequestDto;
import com.quickhr.dto.request.isApprovedRequestLeaveRequestDto;
import com.quickhr.dto.response.AnnualLeaveDetailsDto;
import com.quickhr.entity.Employee;
import com.quickhr.entity.Permission;
import com.quickhr.entity.User;
import com.quickhr.enums.permissions.EPermissionPolicy;
import com.quickhr.enums.permissions.EPermissionState;
import com.quickhr.enums.permissions.EPermissionType;
import com.quickhr.enums.user.EUserRole;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.mapper.PermissionMapper;
import com.quickhr.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionService {

	private final PermissionRepository permissionRepository;
	private final UserService userService;
	private final EmployeeService employeeService;

	public Permission save(Permission permission) {
		return permissionRepository.save(permission);
	}

	public Optional<Permission> getPermissionById(Long id) {
		return permissionRepository.findById(id);
	}
	@Transactional
	public String getLeavesBalance(String token) {
		User employee_user = userService.getUserFromToken(token);

		Employee employee = employeeService.getEmployeeByUserId(employee_user.getId())
				.orElseThrow(() -> new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND));
		return EPermissionPolicy.getAnnualDetail(employee.getDateOfEmployment());
	}

	@Transactional
	public Permission getLeavesDetail(String token, Long permissionId) {
		// Token'dan kullanıcıyı bul
		User user = userService.getUserFromToken(token);


		// Permission'ı service aracılığıyla getir
		Permission permission = getPermissionById(permissionId)
				.orElseThrow(() -> new HRAppException(ErrorType.PERMISSION_NOT_FOUND));

		// Permission bu çalışana mı ait kontrolü
		if (!permission.getUserId().equals(user.getId())) {
			throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
		}

		return permission;
	}

	@Transactional
	public Boolean createWorkHoliday(String token, @Valid CreateLeaveRequestDto dto) {
		// Token'dan kullanıcıyı bul
		User employee_user = userService.getUserFromToken(token);
		Employee employee = employeeService.getEmployeeByUserId(employee_user.getId())
				.orElseThrow(() -> new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND));

		// Çalışanın daha önce yapılmış ve beklemede olan izin talebi var mı?
		boolean hasPendingRequest = permissionRepository.existsByUserIdAndPermissionState(
				employee.getId(), EPermissionState.PENDING);

		if (hasPendingRequest) {
			throw new HRAppException(ErrorType.ALREADY_HAS_PENDING_LEAVE_REQUEST);
		}

		// Kalan izin
		int remainingLeave = getAnnualLeaveDetailsDto(employee).remainingLeave();

		// Talep edilen izin günü sayısı
		int requestedLeave = (int) ChronoUnit.DAYS.between(dto.beginDate(), dto.endDate()) + 1;

		// Talep edilen izin, kalan izinden fazla mı?
		if (requestedLeave > remainingLeave) {
			throw new HRAppException(ErrorType.INSUFFICIENT_LEAVE_BALANCE);
		}

		Permission permission = PermissionMapper.INSTANCE.toPermission(dto, employee_user.getId());
		permission.setPermissionState(EPermissionState.PENDING);
		permissionRepository.save(permission);
		return true;
	}

	public AnnualLeaveDetailsDto getAnnualLeavesDetail(String token) {

		User employee_user = userService.getUserFromToken(token);
		Employee employee = employeeService.getEmployeeByUserId(employee_user.getId())
				.orElseThrow(() -> new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND));

		return getAnnualLeaveDetailsDto(employee);
	}
	public List<Permission> getAllMyLeaves(String token) {
		User user = userService.getUserFromToken(token);

		return permissionRepository.findAllByUserIdAndPermissionStateNot(
				user.getId(),
				EPermissionState.REJECTED
		);
	}

	private AnnualLeaveDetailsDto getAnnualLeaveDetailsDto(Employee employee) {
		int totalLeaves = EPermissionPolicy.getAnnualLeaveDays(employee.getDateOfEmployment());

		// Bu yılın başı ve sonu
		LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
		LocalDate endOfYear = LocalDate.now().withMonth(12).withDayOfMonth(31);

		List<Permission> usedLeaves = permissionRepository.findAllByUserIdAndPermissionTypeAndPermissionStateAndBeginDateBetween(
				employee.getUserId(),
				EPermissionType.ANNUAL_LEAVE,
				EPermissionState.APPROVED,
				startOfYear,
				endOfYear
		);

		// Gün toplamı
		int used_leaves =  usedLeaves.stream()
				.mapToInt(p -> (int) (p.getEndDate().toEpochDay() - p.getBeginDate().toEpochDay() + 1)) // her iki gün dahil
				.sum();
		int remainingLeaves = totalLeaves - used_leaves;

		return new AnnualLeaveDetailsDto(totalLeaves, used_leaves, remainingLeaves);
	}

	@Transactional
	public List<Permission> pendingLeave(String token) {
		User userFromToken = userService.getUserFromToken(token);
		if(userFromToken.getRole() != EUserRole.MANAGER) {
			throw new HRAppException(ErrorType.USER_NOT_MANAGER);
		}
		if(userFromToken.getCompanyId() == null) {
			throw new HRAppException(ErrorType.COMPANY_OR_EMPLOYEE_NOT_FOUND);
		}
		return permissionRepository.findAllByPermissionState(EPermissionState.PENDING, userFromToken.getCompanyId());
	}

	@Transactional
	public List<Permission> approvedLeave(String token) {
		User userFromToken = userService.getUserFromToken(token);
		if(userFromToken.getRole() != EUserRole.MANAGER) {
			throw new HRAppException(ErrorType.USER_NOT_MANAGER);
		}
		if(userFromToken.getCompanyId() == null) {
			throw new HRAppException(ErrorType.COMPANY_OR_EMPLOYEE_NOT_FOUND);
		}
		return permissionRepository.findAllByPermissionState(EPermissionState.APPROVED, userFromToken.getCompanyId());
	}

	@Transactional
	public Boolean isApprovedRequestLeave(String token, isApprovedRequestLeaveRequestDto dto) {
		User userFromToken = userService.getUserFromToken(token);

		Permission permission = permissionRepository.findById(dto.id())
				.orElseThrow(() -> new HRAppException(ErrorType.PERMISSION_NOT_FOUND));

		Optional<User> userFromDto = userService.findUserById(permission.getUserId());

		if (userFromDto.isPresent()) {
			User user = userFromDto.get();
			if(!Objects.equals(user.getCompanyId(), userFromToken.getCompanyId())) {
				throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
			}
		} else {
			throw new HRAppException(ErrorType.USER_NOT_FOUND);
		}

		if(permission.getPermissionState() != (EPermissionState.PENDING)) {
			throw new HRAppException(ErrorType.PERMISSION_STATE_DOESNT_PENDING);
		}
		permission.setPermissionState(dto.isApproved() ? EPermissionState.APPROVED : EPermissionState.REJECTED);
		return dto.isApproved();
	}

}
