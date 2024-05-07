package com.ssafy.nhdream.common.utils;

import com.ssafy.nhdream.entity.loan.LoanAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanAccountNumRepository extends JpaRepository<LoanAccount, Integer> {

}
