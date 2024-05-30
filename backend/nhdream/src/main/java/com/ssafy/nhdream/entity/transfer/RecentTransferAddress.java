package com.ssafy.nhdream.entity.transfer;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import com.ssafy.nhdream.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recent_transfer_address")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentTransferAddress extends BaseTimeEntity {

    //주식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //유저FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //상대방이름
    @Column(name = "recpient_name", nullable = false)
    private String recipientName;

    //상대방주소
    @Column(name = "recipient_address", nullable = false)
    private String recipientAddress;

    @Builder
    public RecentTransferAddress(User user, String recipientName, String recipientAddress) {
        this.user = user;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
    }

    public void updateRecentTransferAddressUpdatedAt() {
        this.updateUpdatedAt();
    }
}
