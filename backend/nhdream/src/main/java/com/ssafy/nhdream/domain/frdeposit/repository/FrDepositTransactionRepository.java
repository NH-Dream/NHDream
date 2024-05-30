package com.ssafy.nhdream.domain.frdeposit.repository;

import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.frdeposit.FrDepositTransaction;
import com.ssafy.nhdream.entity.saving.SavingAccount;
import com.ssafy.nhdream.entity.saving.SavingTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FrDepositTransactionRepository extends JpaRepository<FrDepositTransaction, Integer> {
    @Query("SELECT ft FROM FrDepositTransaction ft WHERE ft.frDepositAccount = :frDepositAccount ORDER BY ft.updatedAt DESC")
    Page<FrDepositTransaction> getFrDepositTransactionList(FrDepositAccount frDepositAccount, Pageable pageable);

    @Query("SELECT ft FROM FrDepositTransaction ft WHERE ft.frDepositAccount.user.id = :id")
    List<FrDepositTransaction> getMyFrDepositTransactionList(int id);

}
