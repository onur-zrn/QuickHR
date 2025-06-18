package com.quickhr.repository;

import com.quickhr.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissonRepository extends JpaRepository<Permission, Long> {

}
