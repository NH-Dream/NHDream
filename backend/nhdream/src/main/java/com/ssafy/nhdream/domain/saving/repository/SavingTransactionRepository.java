package com.ssafy.nhdream.domain.saving.repository;

import com.ssafy.nhdream.entity.saving.SavingAccount;
import com.ssafy.nhdream.entity.saving.SavingTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SavingTransactionRepository extends JpaRepository<SavingTransaction, Integer> {

    //거래내역조회용
    @Query("SELECT st FROM SavingTransaction st WHERE st.savingAccount = :savingAccount ORDER BY st.updatedAt DESC")
    Page<SavingTransaction> getSavingTransactionList(SavingAccount savingAccount, Pageable pageable);

    @Query("SELECT st FROM SavingTransaction st WHERE st.savingAccount.user.id = :id")
    List<SavingTransaction> getMySavingTransactionList (int id);

}
