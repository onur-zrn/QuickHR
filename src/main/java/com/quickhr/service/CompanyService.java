package com.quickhr.service;

import com.quickhr.dto.response.CompanyDashboardResponseDto;
import com.quickhr.entity.*;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.repository.*;
import com.quickhr.enums.company.*;
import com.quickhr.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository companyRepository;
	private final MemberShipService memberShipService;
	private final UserService userService;

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


}
