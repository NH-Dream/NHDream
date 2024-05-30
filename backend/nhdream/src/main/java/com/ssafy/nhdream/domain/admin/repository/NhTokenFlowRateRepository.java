package com.ssafy.nhdream.domain.admin.repository;

import com.ssafy.nhdream.entity.admin.NhTokenFlowRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface NhTokenFlowRateRepository extends JpaRepository<NhTokenFlowRate,Integer> {
    @Query("SELECT ntfr FROM NhTokenFlowRate ntfr WHERE ntfr.referenceDate BETWEEN :startDate and :endDate ORDER BY ntfr.referenceDate")
    List<NhTokenFlowRate> findNhTokenFlowRateListByDates(LocalDate startDate, LocalDate endDate);
}
