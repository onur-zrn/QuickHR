package com.quickhr.repository;

import com.quickhr.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUserId(Long userId);
    List<Employee> findByUserIdIn(List<Long> userIds);
}
