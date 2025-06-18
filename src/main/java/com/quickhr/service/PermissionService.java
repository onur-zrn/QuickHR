package com.quickhr.service;

import com.quickhr.dto.request.CreateLeaveRequestDto;
import com.quickhr.dto.response.AnnualLeaveDetailsDto;
import com.quickhr.entity.Employee;
import com.quickhr.entity.Permission;
import com.quickhr.entity.User;
import com.quickhr.enums.permissions.EPermissionPolicy;
import com.quickhr.enums.permissions.EPermissionState;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.mapper.PermissionMapper;
import com.quickhr.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

	public Boolean createWorkHoliday(String token, @Valid CreateLeaveRequestDto dto) {
		// Token'dan kullanıcıyı bul
		User user = userService.getUserFromToken(token);
		Permission permission = PermissionMapper.INSTANCE.toPermission(dto, user.getId());
		permission.setPermissionState(EPermissionState.PENDING);

		permissionRepository.save(permission);
		return true;


	}

	//burada toplam izin ve kalan izin olacak DEVAMINI YAP
	public AnnualLeaveDetailsDto getAnnualLeavesDetail(String token) {
		User employee_user = userService.getUserFromToken(token);
		Employee employee = employeeService.getEmployeeByUserId(employee_user.getId())
				.orElseThrow(() -> new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND));
		int totalLeaves = EPermissionPolicy.getAnnualLeaveDays(employee.getDateOfEmployment());
		int remainingLeaves; //DTO YAP VE DÖN.
		return null;
	}
}
