package com.ssafy.nhdream.domain.admin.repository;

import com.ssafy.nhdream.entity.admin.NhTokenTradingVolume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface NhTokenTradingVolumeRepository extends JpaRepository<NhTokenTradingVolume, Integer> {
    @Query("SELECT nttv FROM NhTokenTradingVolume nttv WHERE nttv.referenceDate between :startDate and :endDate ORDER BY nttv.referenceDate")
    List<NhTokenTradingVolume> findNhTokenTradingVolumeByDates(LocalDate startDate, LocalDate endDate);
}
