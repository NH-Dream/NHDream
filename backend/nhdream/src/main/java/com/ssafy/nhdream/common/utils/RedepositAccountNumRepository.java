package com.ssafy.nhdream.common.utils;

import com.ssafy.nhdream.entity.redeposit.ReDepositAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedepositAccountNumRepository extends JpaRepository<ReDepositAccount, Integer> {

}
