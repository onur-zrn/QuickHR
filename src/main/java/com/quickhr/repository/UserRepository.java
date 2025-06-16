package com.quickhr.repository;

import com.quickhr.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findById(Long id);
	
	List<User> findAllById(Long id);
	
	boolean existsByMail(String mail);
	
	boolean existsByPhone(String phone);
	
	Optional<User> findOptionalByMail(String mail);
	@Query("select u.id from User u where u.companyId = :companyId")
	List<Long> findUserIdByCompanyId(@Param("companyId") Long companyId);
}
