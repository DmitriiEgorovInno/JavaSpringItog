package org.example.repository;

import org.example.dto.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit,Long> {

    Optional<Limit> findTopByUserIdOrderByCreatedAtDesc(Long userId);

    <S extends Limit> S save(S entity);

    void deleteByUserId(Long userId);

    @Modifying
    @Query(value = "UPDATE Limit l SET l.value = :initialUserLimit")
    void updateAllLimits(BigDecimal initialUserLimit);
}
