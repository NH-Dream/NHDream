    package com.ssafy.nhdream.entity.redeposit;

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
    @Table(name = "re_deposit_account")
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public class ReDepositAccount extends BaseTimeEntity {

        // PK
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Integer id;

        // FK(회원 PK)
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name ="user_id")
        private User user;

        // FK(정기예금옵션 PK)
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "re_deposit_option_id")
        private ReDepositOptions reDepositOptions;

        // 삭제일자
        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;

        // 만료일자
        @Column(name = "expiration_at", nullable = false)
        private LocalDate expirationAt;

        // 정기예금 계좌번호
        @Column(name ="re_deposit_account_num", nullable = false)
        private String accountNum;

        // 정기예금금액
        @Column(name ="re_deposit_amount", nullable = false)
        private BigDecimal amount;

        // 정기예금이자
        @Column(name ="re_deposit_interest", nullable = false)
        private BigDecimal interest;

        // 정기예금원리금
        @Column(name ="re_deposit_balance", nullable = false)
        private BigDecimal balance;

        // 정기예금이자율
        @Column(name ="re_deposit_interest_rate", nullable = false, precision = 8, scale = 4)
        private BigDecimal interestRate;

        //정기예금 계약주소
        @Column(name = "re_deposit_contractaddress", nullable = false)
        private String contractAddress;

        // 만기여부
        @Column(name = "re_deposit_is_active", nullable = false)
        private boolean isActive = true;

        @Builder
        public ReDepositAccount(User user, ReDepositOptions reDepositOptions, LocalDate expirationAt, String accountNum, BigDecimal amount, BigDecimal interest, BigDecimal balance, BigDecimal interestRate, String contractAddress) {
            this.user = user;
            this.reDepositOptions = reDepositOptions;
            this.expirationAt = expirationAt;
            this.accountNum = accountNum;
            this.amount = amount;
            this.interest = interest;
            this.balance = balance;
            this.interestRate = interestRate;
            this.contractAddress = contractAddress;
        }

        public void updateActiveStatus(boolean activeStatus) {this.isActive = activeStatus;};

        public void transferFromFrDepositIncreaseBalance(BigDecimal monthlyAmount) {
            this.balance = this.balance.add(monthlyAmount);
        }

        public boolean withdrawOnMaturity() {
            if (LocalDate.now().isBefore(expirationAt)) return false;

            this.balance = BigDecimal.ZERO;
            this.isActive = false;
            this.deletedAt = LocalDateTime.now();
            return true;
        }

        public boolean withdrawInterestOnMaturity() {
            if (this.isActive) return false;

            this.interest = BigDecimal.ZERO;
            return true;
        }
    }
