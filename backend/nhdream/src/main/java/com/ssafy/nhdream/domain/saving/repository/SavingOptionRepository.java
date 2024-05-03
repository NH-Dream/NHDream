package com.ssafy.nhdream.domain.saving.repository;

import com.ssafy.nhdream.entity.saving.SavingOptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingOptionRepository extends JpaRepository<SavingOptions,Integer> {
}
