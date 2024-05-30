package com.ssafy.nhdream.entity.frdeposit;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import com.ssafy.nhdream.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "FR_DEPOSIT_ACCOUNT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FrDepositAccount extends BaseTimeEntity {

    //주식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //유저FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //자유입출금(지갑) 계좌 컨트랙트주소
    @Column(name = "deposit_contract_address", nullable = false)
    private String contractAddress;

    //계좌 잔액
    @Column(name = "deposit_balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    //활성 상태
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    //자유입출금 계좌번호
    @Column(name = "deposit_account_num", nullable = false)
    private String accountNum;

    @Builder
    public FrDepositAccount(User user, String contractAddress, String accountNum, BigDecimal balance) {
        this.user = user;
        this.contractAddress = contractAddress;
        this.accountNum = accountNum;
        this.balance = balance;
    }

    public void updateFrDepositAccountIsActive() {
        this.isActive = false;
    }

    //이체시 계좌 증가함수
    public void transferIncreaseFrDepositBalance(BigDecimal transferAmount) {
        this.balance = this.balance.add(transferAmount);
    }

    //이체시 계좌 감소함수
    public void transferDecreaseFrDepositBalance(BigDecimal transferAmount) {
        this.balance = this.balance.subtract(transferAmount);
    }

}
