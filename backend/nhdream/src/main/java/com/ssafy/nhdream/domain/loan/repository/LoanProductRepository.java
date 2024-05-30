package com.ssafy.nhdream.domain.loan.repository;

import com.ssafy.nhdream.entity.loan.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProduct, Integer> {

    @Query("SELECT l FROM LoanProduct l")
    List<LoanProduct> findAllByLoanProduct();

    @Transactional
    @Modifying
    @Query("UPDATE LoanProduct p SET p.name = :name, p.amountRange = :amountRange, p.minRate = :minRate, p.preferredRate1 = :preferredRate1, p.preferredRate2 = :preferredRate2 WHERE p.id = :id")
    void updateLoanProductById(int id, String name, BigDecimal amountRange, BigDecimal minRate, BigDecimal preferredRate1, BigDecimal preferredRate2);


    @Transactional
    @Modifying
    @Query("UPDATE LoanProduct p SET p.deletedAt = :deletedAt WHERE p.id = :id")
    void deleteLoanProductById(int id, LocalDate deletedAt);
}
