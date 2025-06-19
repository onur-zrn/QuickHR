package com.quickhr.repository;

import com.quickhr.entity.*;
import com.quickhr.enums.company.ECompanyState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	
	Optional<Company> findByName(String name);

	List <Company> findAllByCompanyState(ECompanyState companyState);

	@Query("SELECT COUNT(c) FROM Company c ")
	Long countCompanies();

	@Query("SELECT COUNT(c) FROM Company c WHERE c.companyState = com.quickhr.enums.company.ECompanyState.ACCEPTED")
	Long countActiveCompanies();
}
