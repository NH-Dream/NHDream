package com.ssafy.nhdream.domain.redeposit.repository;

import com.ssafy.nhdream.domain.user.dto.GetMyAccountResDto;
import com.ssafy.nhdream.entity.redeposit.ReDepositAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReDepositAccountRepository extends JpaRepository<ReDepositAccount, Integer> {

    //가입한 전체 예적금 계좌 조회용
    @Query("SELECT new com.ssafy.nhdream.domain.user.dto.GetMyAccountResDto(0,r.id,o.id,p.name,r.balance,r.accountNum,r.contractAddress, r.createdAt) " +
            "FROM ReDepositAccount r " +
            "JOIN r.reDepositOptions o " +
            "JOIN o.reDepositProduct p " +
            "WHERE r.user.id = :userId ORDER BY r.createdAt" )
    List<GetMyAccountResDto> findSavingAccountByUserId(int userId);

    Optional<ReDepositAccount> findByUserIdAndReDepositOptionsId(int userId, int optionId);

    //만기된 적금 계좌 조회용
    @Query("SELECT r FROM ReDepositAccount r WHERE r.expirationAt=:today and r.isActive = true")
    List<ReDepositAccount> expiredAccountList(LocalDate today);
}
