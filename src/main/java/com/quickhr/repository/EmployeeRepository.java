package com.quickhr.repository;

import com.quickhr.entity.*;
import com.quickhr.enums.user.EUserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserId(Long userId);
    Page<Employee> findByUserIdIn(List<Long> userIds, Pageable pageable);
    List<Employee> findAllByCompanyIdAndUserState(Long companyId, EUserState userState);
}
