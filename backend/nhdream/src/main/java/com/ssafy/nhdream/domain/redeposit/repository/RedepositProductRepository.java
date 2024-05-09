package com.ssafy.nhdream.domain.redeposit.repository;

import com.ssafy.nhdream.entity.redeposit.ReDepositProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedepositProductRepository extends JpaRepository<ReDepositProduct, Integer> {
}
