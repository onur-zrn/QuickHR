package com.quickhr.repository;

import com.quickhr.entity.*;
import com.quickhr.enums.company.ECompanyState;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	
	Optional<Company> findByName(String name);

	List <Company> findAllByCompanyState(ECompanyState companyState);
}
