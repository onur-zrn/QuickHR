package com.quickhr.BREAK;

import org.springframework.data.jpa.repository.*;
import java.util.*;

public interface BreakRepository extends JpaRepository<Break, Long> {
	List<Break> findAllByCompanyId(Long companyId);
	
	@Query("SELECT b FROM Break b WHERE b.userId = :userId AND b.companyId = :companyId AND b.breakId <> :breakId")
	Optional<Break> findByUserIdAndCompanyIdAndBreakIdNot(Long userId, Long companyId, Long breakId);
	
	Optional<Break> findByUserIdAndCompanyId(Long userId, Long companyId);
	
	@Query("SELECT b FROM Break b WHERE b.userId IS NOT NULL AND b.companyId = :companyId")
	List<Break> findAllAssignedBreaksByCompanyId(Long companyId);
}
