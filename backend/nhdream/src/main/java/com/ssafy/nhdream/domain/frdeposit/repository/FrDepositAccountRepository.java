package com.ssafy.nhdream.domain.frdeposit.repository;

import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FrDepositAccountRepository extends JpaRepository<FrDepositAccount,Integer> {

    Optional<FrDepositAccount> findFrDepositAccountByContractAddress(String walletAddress);
}
