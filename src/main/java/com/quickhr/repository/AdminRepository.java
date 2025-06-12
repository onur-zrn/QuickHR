package com.quickhr.repository;

import com.quickhr.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByUsername(String username);

    Optional<Admin> findOptionalByUsername(String username);

}
