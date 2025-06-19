package com.quickhr.repository;

import com.quickhr.entity.Permission;
import com.quickhr.enums.permissions.EPermissionState;
import com.quickhr.enums.permissions.EPermissionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findAllByUserIdAndPermissionTypeAndPermissionStateAndBeginDateBetween(Long userId, EPermissionType ePermissionType, EPermissionState ePermissionState, LocalDate startOfYear, LocalDate endOfYear);

    List<Permission> findAllByUserIdAndPermissionStateNot(Long userId, EPermissionState state);

    boolean existsByUserIdAndPermissionState(Long userId, EPermissionState state);
}
