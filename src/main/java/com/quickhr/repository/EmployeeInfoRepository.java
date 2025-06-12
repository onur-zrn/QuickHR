package com.quickhr.repository;

import com.quickhr.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, Integer> {

}
