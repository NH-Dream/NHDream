package com.ssafy.nhdream.domain.redeposit.repository;

import com.ssafy.nhdream.entity.redeposit.ReDepositOptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedepositOptionRepository  extends JpaRepository<ReDepositOptions, Integer> {
}
