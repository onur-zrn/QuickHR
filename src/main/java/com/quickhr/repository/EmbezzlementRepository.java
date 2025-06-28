package com.quickhr.repository;

import com.quickhr.entity.Embezzlement;
import com.quickhr.enums.embezzlement.EEmbezzlementState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmbezzlementRepository extends JpaRepository<Embezzlement, Long> {

    @Query("""
            SELECT e FROM Embezzlement e WHERE e.embezzlementState = :embezzlementState AND e.userId
            IN (SELECT u.id FROM User u WHERE u.companyId = :companyId)
            """)
    List<Embezzlement> findAllByEmbezzlementState(@Param("companyId") Long companyId ,@Param("embezzlementState") EEmbezzlementState embezzlementState);
    @Query("""
            SELECT e FROM Embezzlement e WHERE e.embezzlementState <> :excludedState AND e.userId
            IN (SELECT u.id FROM User u WHERE u.companyId = :companyId)
            """)
    List<Embezzlement> findAllByEmbezzlementStateNot( @Param("companyId") Long companyId, @Param("excludedState") EEmbezzlementState excludedState);

    @Query("SELECT e FROM Embezzlement e WHERE e.companyId = :companyId")
    List<Embezzlement> findAllByCompanyId(@Param("companyId") Long companyId);

    Optional<Embezzlement> findById(Long id);

    List<Embezzlement> findAllByUserId(Long id);

}