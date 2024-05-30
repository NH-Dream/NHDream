package com.ssafy.nhdream.domain.loan.repository;

import com.ssafy.nhdream.entity.loan.LoanTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanTransactionRepository extends JpaRepository<LoanTransaction, Integer> {

    @Query("SELECT l FROM LoanTransaction l WHERE l.loanAccount.user.id = :id")
    List<LoanTransaction> getMyLoanTransactionList (int id);

}
