package com.quickhr.repository;

import com.quickhr.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findById(Long id);
	
	List<User> findAllById(Long id);
	
	boolean existsByMail(String mail);
	
	boolean existsByPhone(String phone);
	
	Optional<User> findOptionalByMail(String mail);

}
