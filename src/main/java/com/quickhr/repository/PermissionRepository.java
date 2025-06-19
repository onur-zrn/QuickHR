package com.quickhr.repository;

import com.quickhr.entity.Permission;
import com.quickhr.enums.permissions.EPermissionState;
import com.quickhr.enums.permissions.EPermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findAllByUserIdAndPermissionTypeAndPermissionStateAndBeginDateBetween(Long userId, EPermissionType ePermissionType, EPermissionState ePermissionState, LocalDate startOfYear, LocalDate endOfYear);

    List<Permission> findAllByUserIdAndPermissionStateNot(Long userId, EPermissionState state);

    boolean existsByUserIdAndPermissionState(Long userId, EPermissionState state);

    @Query("""
			SELECT p FROM Permission p WHERE p.permissionState = :state AND p.userId
			IN (SELECT u.id FROM User u WHERE u.companyId = :companyId)
		""")
    List<Permission> findAllByPermissionState(@Param("state") EPermissionState state, @Param("companyId") Long companyId);
}
