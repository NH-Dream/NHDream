package com.ssafy.nhdream.domain.loan.repository;

import com.ssafy.nhdream.entity.loan.LoanAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, Integer> {

    @Query("SELECT l FROM LoanAccount l WHERE l.loanApproval.id = :id")
    LoanAccount findByApprovalId(int id);:

}
