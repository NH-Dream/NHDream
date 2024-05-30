package com.ssafy.nhdream.domain.redeposit.repository;

import com.ssafy.nhdream.entity.redeposit.ReDepositAccount;
import com.ssafy.nhdream.entity.redeposit.ReDepositTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RedepositTransactionRepository extends JpaRepository<ReDepositTransaction, Integer> {

    @Query("SELECT rt FROM ReDepositTransaction rt WHERE rt.reDepositAccount = :reDepositAccount ORDER BY rt.updatedAt DESC")
    List<ReDepositTransaction> getDepositTransactionList(ReDepositAccount reDepositAccount);

    @Query("SELECT rt FROM ReDepositTransaction rt WHERE rt.reDepositAccount.user.id = :id")
    List<ReDepositTransaction> getMyReDepositTransaction (int id);

}
