package com.ssafy.nhdream.domain.redeposit.repository;

import com.ssafy.nhdream.domain.redeposit.dto.RedepositProductListDto;
import com.ssafy.nhdream.entity.redeposit.ReDepositProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RedepositProductRepository extends JpaRepository<ReDepositProduct, Integer> {

    @Query("SELECT new com.ssafy.nhdream.domain.redeposit.dto.RedepositProductListDto(rp.id, rp.name, " +
            "GREATEST(ro.rate + ro.preferredRate1, ro.rate + ro.preferredRate2), rp.amountRange) " +
            "FROM ReDepositProduct rp " +
            "JOIN rp.reDepositOptions ro " +
            "WHERE ro.term = 24 AND rp.isActive = true")
    List<RedepositProductListDto> findReDepositProductsWithTerm24();

}