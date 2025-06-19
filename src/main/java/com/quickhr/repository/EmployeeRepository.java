package com.quickhr.repository;

import com.quickhr.entity.*;
import com.quickhr.enums.user.EUserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserId(Long userId);
    Page<Employee> findByUserIdIn(List<Long> userIds, Pageable pageable);
    List<Employee> findAllByCompanyIdAndUserState(Long companyId, EUserState userState);

    @Query("SELECT COUNT(e) FROM Employee e")
    Long countPersonal();

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.userState = com.quickhr.enums.user.EUserState.ACTIVE")
    Long countActivePersonal();

    @Query("""
    SELECT COUNT(e) 
    FROM Employee e 
    WHERE e.userState = com.quickhr.enums.user.EUserState.ACTIVE 
      AND e.companyId = :companyId
""")
    Long countActivePersonalByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(p) FROM Permission p WHERE CURRENT_DATE BETWEEN p.beginDate AND p.endDate AND p.permissionState = com.quickhr.enums.permissions.EPermissionState.APPROVED")
    Long countApprovedPermissionsToday();

}
