package com.quickhr.service;

import com.quickhr.dto.request.EmployeeRequestDto;
import com.quickhr.dto.response.CompanyDashboardResponseDto;
import com.quickhr.dto.response.EmployeeResponseDto;
import com.quickhr.entity.*;
import com.quickhr.enums.user.EUserRole;
import com.quickhr.enums.user.EUserState;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.mapper.EmployeeMapper;
import com.quickhr.mapper.UserMapper;
import com.quickhr.repository.*;
import com.quickhr.enums.company.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository companyRepository;
	private final MemberShipService memberShipService;
	private final UserService userService;
	private final EmployeeService employeeService;
	private final PasswordEncoder passwordEncoder;

	public Company save(Company company) {
		return companyRepository.save(company);
	}

	public List<Company> findAllCompany() {
		return companyRepository.findAll();
	}

	public Optional<Company> getCompanyByName(String name) {
		return companyRepository.findByName(name);
	}

	public Optional<Company> findCompanyByCompanyName(String companyName) {
		return companyRepository.findByName(companyName);
	}

	public Optional<Company> getCompanyById(Long id) {
		return companyRepository.findById(id);
	}

	public Company createCompany(String companyName) {
		Optional<Company> companyOptional = getCompanyByName(companyName);
		if (companyOptional.isPresent()) {
			memberShipService.createOrFindMemberShip(companyOptional.get().getId());
			return companyOptional.get();
		}
		
		Company company = Company.builder()
		                         .name(companyName)
		                         .companyState(ECompanyState.PENDING)
		                         .build();
		company = companyRepository.save(company);
		memberShipService.createMemberShip(company.getId());
		return company;
	}

	public CompanyDashboardResponseDto getCompanyDashboard(String token) {
		userService.getUserFromToken(token);
		return new CompanyDashboardResponseDto(
				"Company Dashboard",
				120,     // Total employees
				15,                  // Pending leave requests
				List.of(
						"New Year (Jan 1)",
						"National Holiday (Apr 23)"
				),
				List.of(
						"Monthly performance review scheduled",
						"New HR policy update"
				)
		);
	}

	public List<Company> findAllByCompanyState(ECompanyState state) {
		return companyRepository.findAllByCompanyState(state);
	}

	@Transactional
	public List<EmployeeResponseDto> getEmployeeInCompany(String token) {
		User user = userService.getUserFromToken(token);
		if (!user.getRole().equals(EUserRole.MANAGER)) {
			throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
		}

		if (user.getCompanyId() == null) {
			throw new HRAppException(ErrorType.COMPANY_OR_EMPLOYEE_NOT_FOUND);
		}

		// 1. Şirketteki userId listesi
		List<Long> userIds = userService.getUserIdsByCompanyId(user.getCompanyId());

		return employeeService.getEmployeeInCompany(userIds);

	}
	@Transactional
	public EmployeeResponseDto getEmployeeDetailsById(String token, Long id) {
		User user = userService.getUserFromToken(token);

		if(!user.getRole().equals(EUserRole.MANAGER)) {
			throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
		}

		if (user.getCompanyId() == null) {
			throw new HRAppException(ErrorType.COMPANY_OR_EMPLOYEE_NOT_FOUND);
		}

		List<Long> userIdsByCompanyId = userService.getUserIdsByCompanyId(user.getCompanyId());

		if (!userIdsByCompanyId.contains(id)) {
			throw new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND);
		}

		Employee employeeInCompany = employeeService.getEmployeeInCompany(id);
		if (employeeInCompany == null) {
			throw new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND);
		}
		return EmployeeMapper.INSTANCE.toDto(employeeInCompany);
	}

	@Transactional
	public EmployeeResponseDto addEmployee(String token, EmployeeRequestDto dto) {
		User manager = userService.getUserFromToken(token);

		if (manager.getRole() != EUserRole.MANAGER) {
			throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
		}

		if (userService.existsByMail(dto.getMail())) {
			throw new HRAppException(ErrorType.ALREADY_EXIST_USER_MAIL);
		}

		User user = UserMapper.INSTANCE.fromEmployeeRequestDto(dto);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setCompanyId(manager.getCompanyId());
		user.setRole(EUserRole.PERSONAL);
		user.setUserState(EUserState.ACTIVE);
		userService.save(user);

		// ⬇️ Burada mapper ile dönüştürme yapılıyor
		Employee employee = EmployeeMapper.INSTANCE.fromDto(dto);
		employee.setUserId(user.getId());

		employeeService.save(employee);

		return EmployeeMapper.INSTANCE.toDto(employee);
	}


}
