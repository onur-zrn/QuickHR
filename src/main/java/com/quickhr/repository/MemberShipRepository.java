package com.quickhr.repository;

import com.quickhr.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface MemberShipRepository extends JpaRepository<MemberShip, Long> {
	
	Optional<MemberShip> findByCompanyId(Long companyId);
}
