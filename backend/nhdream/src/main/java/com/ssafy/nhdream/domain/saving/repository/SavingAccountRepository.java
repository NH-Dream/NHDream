package com.ssafy.nhdream.domain.saving.repository;

import com.ssafy.nhdream.domain.user.dto.GetMyAccountResDto;
import com.ssafy.nhdream.entity.saving.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SavingAccountRepository extends JpaRepository<SavingAccount, Integer> {
    //가입한 예적금 전체 조회용
    @Query("SELECT new com.ssafy.nhdream.domain.user.dto.GetMyAccountResDto(1,s.id,o.id,p.name,s.balance,s.accountNum,s.contractAddress, s.createdAt) " +
            "FROM SavingAccount s " +
            "JOIN s.savingOptions o " +
            "JOIN o.savingProduct p " +
            "WHERE s.user.id = :userId " +
            "ORDER BY s.createdAt")
    List<GetMyAccountResDto> findSavingAccountByUserId(int userId);

    Optional<SavingAccount> findByUserIdAndSavingOptionsId(int userId, int savingOptionId);

    //만기된 적금 계좌 조회용
    @Query("SELECT s FROM SavingAccount s WHERE s.expireAt=:today and s.isActive =true ")
    List<SavingAccount> expiredAccountList(LocalDate today);

    //이자줄 적금 계좌 조회용
    @Query("SELECT s FROM SavingAccount  s WHERE s.nextInterestDate=:today and s.isActive=true ")
    List<SavingAccount> accountsToReceiveInterest(LocalDate today);

    //계좌번호로 계좌찾기
    Optional<SavingAccount> findByAccountNum(String accountNum);

    //해당상품으로 가입한 계좌가 있는지 조회
    boolean existsByUserIdAndSavingOptions_SavingProduct_Id(Integer userId, Integer productId);

}

