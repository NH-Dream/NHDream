package com.ssafy.nhdream.entity.transfer;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "automatic_transfer_task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutomaticTransferTask extends BaseTimeEntity {

    //식별Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //보낼계좌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_wallet_account_id", nullable = false)
    private FrDepositAccount senderWalletAccount;

    //받을계좌
    @Column(name = "recipient_wallet_address", nullable = false)
    private String recipientAccount;
    //받을 계좌주
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    //회차당 보내는 금액
    @Column(name = "recurring_amount", nullable = false)
    private BigDecimal recurringAmount;

    //매월 납기일
    @Column(name = "transferday", nullable = false)
    private int transferDay;

    //예정된 이체시간
    @Column(name = "next_schedule_time", nullable = false)
    private LocalDate nextScheduleTime;

    //시작일
    @Column(name = "started_at",nullable = false)
    private LocalDate startedAt;

    //만료일
    @Column(name = "expired_at", nullable = false)
    private LocalDate expiredAt;

    //기간
    @Column(name = "term",nullable = false)
    private int term;

    //활성상태
    @Column(name = "is_active",nullable = false)
    private boolean isActive=true;

    //타입 0이면 자유입출금 1이면 적금 2면 대출
    @Column(name = "type", nullable = false)
    private int type = 0;

    @Builder
    public AutomaticTransferTask(User user, FrDepositAccount senderWalletAccount, String recipientAccount,
                                 String recipientName,BigDecimal recurringAmount, int transferDay, LocalDate nextScheduleTime,
                                 LocalDate startedAt, LocalDate expiredAt, int term, int type) {
        this.user = user;
        this.senderWalletAccount = senderWalletAccount;
        this.recipientAccount = recipientAccount;
        this.recipientName = recipientName;
        this.recurringAmount = recurringAmount;
        this.transferDay = transferDay;
        this.nextScheduleTime = nextScheduleTime;
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
        this.term = term;
        this.type = type;
    }

    public void updatedIsActive() {
        this.isActive = false;
    }

    //이체금액이랑 이체기간이랑 날짜??
    public void updateTermAndDateAndAmount(BigDecimal recurringAmount, int transferDay, LocalDate expiredAt, int term, LocalDate nextScheduleTime) {
        this.recurringAmount = recurringAmount;
        this.transferDay = transferDay;
        this.expiredAt = expiredAt;
        this.term = term;
        this.nextScheduleTime = nextScheduleTime;
    }

    public void updateNextScheduleTime(LocalDate nextScheduleTime) {
        this.nextScheduleTime = nextScheduleTime;
    }
}
