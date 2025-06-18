package com.quickhr.init;

import com.quickhr.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
	private final UserRepository userRepository;
	private final AdminRepository adminRepository;
	private final CompanyRepository companyRepository;
	private final EmployeeRepository employeeRepository;
	private final PermissonRepository permissonRepository;
	@PostConstruct
	public void init() {
		if (userRepository.count() == 0) {
			userRepository.saveAll(UserInitializer.userInitializer());
		}

		if (adminRepository.count() == 0) {
			adminRepository.saveAll(AdminInitializer.adminInitializer());
		}

		if (companyRepository.count() == 0) {
			companyRepository.saveAll(CompanyInitializer.companyInitializer());
		}

		if (employeeRepository.count() == 0) {
			employeeRepository.saveAll(EmployeeInitializer.employeeInitializer());
		}

		if (permissonRepository.count() == 0) {
			permissonRepository.saveAll(PermissionInitializer.permissionInitializer());
		}
	}
}
