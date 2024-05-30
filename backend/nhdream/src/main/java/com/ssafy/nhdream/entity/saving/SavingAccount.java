package com.ssafy.nhdream.entity.saving;

import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import com.ssafy.nhdream.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "saving_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingAccount extends BaseTimeEntity {

    //Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //외래키영역
    //유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //상품옵션
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_options_id", nullable = false)
    private SavingOptions savingOptions;

    //삭제일자
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    //만료일
    @Column(name = "expire_at", nullable = false)
    private LocalDate expireAt;

//    //총적금액
//    @Column(name = "saving_amount", nullable = false)
//    private BigDecimal totalAmount;

    //매달 빠져나가는 날
    @Column(name = "depositDay", nullable = false)
    private int depositDay;

    //매월적금액
    @Column(name = "monthly_saving_amount", nullable = false)
    private BigDecimal monthlyAmount;

    //적금총이자
    @Column(name = "saving_interest", nullable = false)
    private BigDecimal interest = BigDecimal.ZERO;

    //적금이자율
    @Column(name = "saving_interest_rate", nullable = false,precision = 8, scale = 4)
    private BigDecimal interestRate;

    //지갑주소
    @Column(name = "saving_contractaddress", nullable = false)
    private String contractAddress;

    //계좌번호
    @Column(name = "saving_account_num", nullable = false, unique = true)
    private String accountNum;

    //활성상태
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    //납입회수
    @Column(name = "saving_installment_count", nullable = false)
    private int installmentCount = 0;

    //매달 납입일
    @Column(name = "monthly_deposit_date", nullable = false)
    private LocalDate nextDepositDate;

    //현재적금액
    @Column(name = "saving_balance", nullable = false)
    private BigDecimal balance;

    //이자지급일
    @Column(name = "next_interest_date")
    private LocalDate nextInterestDate;

    //적금시작일
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    //생성자
    @Builder
    public SavingAccount(LocalDate expireAt, BigDecimal monthlyAmount,
                          BigDecimal interestRate, String contractAddress,
                         String accountNum, User user, SavingOptions savingOptions,int depositDay,
                         BigDecimal balance, int installmentCount, LocalDate nextDepositDate,
                         LocalDate nextInterestDate, LocalDate startDate) {
        this.expireAt = expireAt;
        this.monthlyAmount = monthlyAmount;
        this.interestRate = interestRate;
        this.contractAddress = contractAddress;
        this.accountNum = accountNum;
        this.user = user;
        this.depositDay = depositDay;
        this.savingOptions = savingOptions;
        this.installmentCount = installmentCount;
        this.nextDepositDate = nextDepositDate;
        this.balance = balance;
        this.nextInterestDate = nextInterestDate;
        this.startDate= startDate;
    }

    //함수
    public void updateSavingAccountIsActive() {
        this.isActive = false;
    }

    public void transferFromFrDepositIncreaseBalanceAndUpdateNextDepositDate(BigDecimal monthlyAmount, LocalDate nextScheduleTime) {
        this.balance = this.balance.add(monthlyAmount);
        this.installmentCount += 1;
        this.nextDepositDate = nextScheduleTime;
    }


    public void withdrawOnMaturity() {
        if (LocalDate.now().isBefore(expireAt)) {
            throw new CustomException(ExceptionType.NOT_YET_EXPIRED);
        }
        this.balance = BigDecimal.ZERO;
        this.isActive = false;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateInterestAndNextInterestDate(BigDecimal monthlyInterest, LocalDate nextInterestDate) {
        this.interest = this.interest.add(monthlyInterest);
        this.nextInterestDate = nextInterestDate;
    }

    public void withdrawInterestOnMaturity() {
        if (this.isActive) {
            throw new CustomException(ExceptionType.NOT_YET_EXPIRED);
        }
        this.interest = BigDecimal.ZERO;
    }


}
