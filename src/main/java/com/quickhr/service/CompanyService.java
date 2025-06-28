package com.quickhr.service;

import com.quickhr.dto.request.EmployeeRequestDto;
import com.quickhr.dto.response.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository companyRepository;
	private final MemberShipService memberShipService;
	private final UserService userService;
	private final EmployeeService employeeService;
	private final PublicHolidayService publicHolidayService;
	private final PasswordEncoder passwordEncoder;

	public Company save(Company company) {
		return companyRepository.save(company);
	}

	public List<Company> findAllCompany() {
		return companyRepository.findAll();
	}

	public Long countCompanies() {
		return companyRepository.countCompanies();
	}

	public Long countActiveCompanies() {
		return companyRepository.countActiveCompanies();
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

	public CompanyDashboardResponseDto getCompanyDashboard(String token) {
		User userFromToken = userService.getUserFromToken(token);
		List<PublicHolidayResponseDto> holidayDtos = publicHolidayService.findAll();

		Long companyId = userFromToken.getCompanyId();
		Optional<Company> optionalCompany = getCompanyById(companyId);
		String company_name = "COMPANY DASHBOARD";
		if (optionalCompany.isEmpty()) {
			throw new HRAppException(ErrorType.COMPANY_NOT_FOUND);
		}else
			company_name = optionalCompany.get().getName();

		return  CompanyDashboardResponseDto.of(
				company_name,
				employeeService.countActivePersonalByCompanyId(companyId),
				employeeService.countApprovedPermissionsToday(companyId),
				holidayDtos

		);
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


	public List<Company> findAllByCompanyState(ECompanyState state) {
		return companyRepository.findAllByCompanyState(state);
	}

	@Transactional
	public Page<EmployeeResponseDto> getEmployeeInCompany(String token, Pageable pageable) {
		User user = userService.getUserFromToken(token);

		if (!user.getRole().equals(EUserRole.MANAGER)) {
			throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
		}

		if (user.getCompanyId() == null) {
			throw new HRAppException(ErrorType.COMPANY_OR_EMPLOYEE_NOT_FOUND);
		}

		// Şirketteki tüm userId'leri al
		List<Long> userIds = userService.getUserIdsByCompanyId(user.getCompanyId());

		// EmployeeService içinde pagination ile verileri döndür
		return employeeService.getEmployeeInCompany(userIds, pageable);
	}

	@Transactional
	public EmployeeDetailsResponseDto getEmployeeDetailsById(String token, Long id) {
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

		Optional<Employee> employeeInCompany = employeeService.getEmployeeInCompany(id);
		if (employeeInCompany.isEmpty()) {
			throw new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND);
		}
		return EmployeeMapper.INSTANCE.toDetailsDto(employeeInCompany.get());

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

	@Transactional
	public Boolean makeActivePersonal(String token, Long id) {
		Employee employees = validateManagerAndEmployeeAccess(token, id);
		// Employee için aktiflik mevcut mu değil mi kontrolü
		if (employees.getUserState() == (EUserState.ACTIVE)) {
			throw new HRAppException(ErrorType.EMPLOYEE_ALREADY_EXIST_ACTIVE);
		}
		// Employee pending modda mı değil mi kontrol edilir.BURAYA BAK.
		if(employees.getUserState() != (EUserState.PENDING)) {
			throw new HRAppException(ErrorType.EMPLOYEE_DOESNT_PENDING);
		}
		// Tüm şartları sağlıyorsa employee durumu aktif yapıyoruz
		employees.setUserState(EUserState.ACTIVE);
		return true;
	}

	@Transactional
	public Boolean makePassivePersonal(String token, Long id) {
		Employee employees = validateManagerAndEmployeeAccess(token, id);
		// Employee için pasiflik mevcut mu değil mi kontrolü
		if (employees.getUserState() == (EUserState.INACTIVE)) {
			throw new HRAppException(ErrorType.EMPLOYEE_ALREADY_EXIST_INACTIVE);
		}
		// Employee pending modda mı değil mi kontrol edilir
		if(employees.getUserState() != (EUserState.PENDING)) {
			throw new HRAppException(ErrorType.EMPLOYEE_DOESNT_PENDING);
		}
		// Tüm şartları sağlıyorsa employee durumu pasif yapıyoruz
		employees.setUserState(EUserState.INACTIVE);
		return true;
	}

	@Transactional
	public Boolean changePersonalStatus(String token, Long id, EUserState newUserState) {
		Employee employees = validateManagerAndEmployeeAccess(token, id);
		// Employee için durum değişikliği kontrolü - aynı değer girilmemeli
		if(employees.getUserState() == newUserState) {
			throw new HRAppException(ErrorType.USER_STATE_SAME);
		}
		employees.setUserState(newUserState);
		return true;
	}

	@Transactional
	public List<PersonalStateResponseDto> getActivePersonal(String token) {
		User usersValid = validateManagerForUserAccess(token);
		Long companyId = usersValid.getCompanyId();

		List<Employee> activeUsersByCompany = employeeService.getActiveUsersByCompany(companyId, EUserState.ACTIVE);
		if(activeUsersByCompany.isEmpty()) {
			throw new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND);
		}

		return activeUsersByCompany.stream()
				.map(user -> new PersonalStateResponseDto(
						user.getId(),
						user.getFirstName(),
						user.getLastName(),
						user.getUserState()
				)).toList();
	}

	@Transactional
	public List<PersonalStateResponseDto> getPassivePersonal(String token) {
		User usersValid = validateManagerForUserAccess(token);
		Long companyId = usersValid.getCompanyId();

		List<Employee> passiveUsersByCompany = employeeService.getPassiveUsersByCompany(companyId, EUserState.INACTIVE);
		if(passiveUsersByCompany.isEmpty()) {
			throw new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND);
		}

		return passiveUsersByCompany.stream()
				.map(user -> new PersonalStateResponseDto(
						user.getId(),
						user.getFirstName(),
						user.getLastName(),
						user.getUserState()
				)).toList();
	}

	@Transactional
	public List<PersonalStateResponseDto> getPendingPersonal(String token) {
		User usersValid = validateManagerForUserAccess(token);
		Long companyId = usersValid.getCompanyId();

		List<Employee> pendingUsersByCompany = employeeService.getPendingUsersByCompany(companyId, EUserState.PENDING);
		if(pendingUsersByCompany.isEmpty()) {
			throw new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND);
		}

		return pendingUsersByCompany.stream()
				.map(user -> new PersonalStateResponseDto(
						user.getId(),
						user.getFirstName(),
						user.getLastName(),
						user.getUserState()
				)).toList();
	}

	@Transactional
	public List<PersonalStateResponseDto> getDeletedPersonal(String token) {
		User usersValid = validateManagerForUserAccess(token);
		Long companyId = usersValid.getCompanyId();

		List<Employee> deletedUsersByCompany = employeeService.getDeletedUsersByCompany(companyId, EUserState.DELETED);
		if(deletedUsersByCompany.isEmpty()) {
			throw new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND);
		}

		return deletedUsersByCompany.stream()
				.map(user -> new PersonalStateResponseDto(
						user.getId(),
						user.getFirstName(),
						user.getLastName(),
						user.getUserState()
				)).toList();
	}

	@Transactional
	public Boolean makeDeletedPersonal(String token, Long id) {
		Employee employees = validateManagerAndEmployeeAccess(token, id);
		// Employee silinmiş mi kontrol edilir
		if (employees.getUserState() == EUserState.DELETED) {
			throw new HRAppException(ErrorType.EMPLOYEE_ALREADY_EXIST_DELETED);
		}
		// Tüm şartları sağlıyorsa employee durumu pasif yapıyoruz
		employees.setUserState(EUserState.DELETED);
		return true;
	}

	private Employee validateManagerAndEmployeeAccess(String token, Long id) {
		User users = validateManagerForUserAccess(token);
		// Employee için kayıtlı personel kontrolü
		Employee employees = employeeService.getEmployeeByUserId(users.getId())
				.orElseThrow(() -> new HRAppException(ErrorType.EMPLOYEE_NOT_FOUND));
		// Manager için employee üzerindeki yetki kontrülü
		if (!users.getCompanyId().equals(employees.getCompanyId())) {
			throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
		}
		return employees;
	}

	private User validateManagerForUserAccess(String token) {
		User users = userService.getUserFromToken(token);
		// Company için aktiflik kontrolü
		//if (!users.getCompanyId().equals(ECompanyState.ACCEPTED)) {
		//	throw new HRAppException(ErrorType.USER_COMPANY_STATE_DOESNT_ACCEPTED);
		//}


		// Token'a sahip user için aktiflik kontrolü
		if (users.getUserState() != EUserState.ACTIVE) {
			throw new HRAppException(ErrorType.USER_STATE_DOESNT_ACTIVE);
		}
		// User manager mı değil mi kontrolü
		if (users.getRole() != EUserRole.MANAGER) {
			throw new HRAppException(ErrorType.USER_NOT_MANAGER);
		}
		return users;
	}


}