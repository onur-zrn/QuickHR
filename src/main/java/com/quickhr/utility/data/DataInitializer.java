package com.quickhr.utility.data;

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
	}
}