package com.ssafy.nhdream.domain.loan.repository;

import com.ssafy.nhdream.entity.loan.LoanAccount;
import com.ssafy.nhdream.entity.saving.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, Integer> {

    @Query("SELECT l FROM LoanAccount l WHERE l.loanApproval.id = :id")
    LoanAccount findByApprovalId(int id);

    @Modifying
    @Query("UPDATE LoanAccount l SET l.balance = :amount, l.outstandingInterest = :interest, l.outstandingPrincipal = :principal, l.round = l.round+1 WHERE l.id = :id")
    void updateBalance(int id, BigDecimal interest, BigDecimal principal, BigDecimal amount);

    @Modifying
    @Query("UPDATE LoanAccount l SET l.state = 1 WHERE l.id = :id")
    void finishLoan(int id);

    //계좌번호로 계좌찾기
    Optional<LoanAccount> findByAccountNum(String accountNum);
}
