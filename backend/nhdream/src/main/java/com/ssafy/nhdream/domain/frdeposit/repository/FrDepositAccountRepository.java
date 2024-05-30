package com.ssafy.nhdream.domain.frdeposit.repository;

import com.ssafy.nhdream.domain.frdeposit.dto.FrDepositBalanceResDto;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FrDepositAccountRepository extends JpaRepository<FrDepositAccount,Integer> {

    Optional<FrDepositAccount> findFrDepositAccountByContractAddress(String walletAddress);
    Optional<FrDepositAccount> findByUser(User user);
}
