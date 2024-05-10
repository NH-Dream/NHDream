package com.ssafy.nhdream.domain.redeposit.repository;

import com.ssafy.nhdream.entity.redeposit.ReDepositTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedepositTransactionRepository extends JpaRepository<ReDepositTransaction, Integer> {
}
