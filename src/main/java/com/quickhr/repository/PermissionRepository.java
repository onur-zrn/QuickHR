package com.quickhr.repository;

import com.quickhr.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
