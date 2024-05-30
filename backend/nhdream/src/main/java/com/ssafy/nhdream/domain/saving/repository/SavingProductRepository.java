package com.ssafy.nhdream.domain.saving.repository;

import com.ssafy.nhdream.domain.saving.dto.SavingProductListResDto;
import com.ssafy.nhdream.entity.saving.SavingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SavingProductRepository extends JpaRepository<SavingProduct, Integer> {

    @Query("SELECT new com.ssafy.nhdream.domain.saving.dto.SavingProductListResDto(sp.id, sp.name, " +
            "GREATEST(so.rate + so.preferredRate1, so.rate + so.preferredRate2), sp.maxMonthlyLimit) " +
            "FROM SavingProduct sp " +
            "JOIN sp.savingOptions so " +
            "WHERE so.term = 24 AND sp.isActive = true")
    List<SavingProductListResDto> findSavingProductsWithTerm24();
}
