package com.ssafy.nhdream.entity.voucher;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="voucher_affiliate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoucherAffiliate extends BaseTimeEntity {

    // ID
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 제휴처 이름
    @Column(name = "affiliate_name", nullable = false)
    private String name;

    // 제휴처 지갑 번호
    @Column(name = "affiliate_wallet_address", nullable = false)
    private String walletAddress;

    // 제휴처 바우처 토큰 잔액
    @Column(name = "affiliate_wallet_privatekey", nullable = false)
    private String walletPrivateKey;

    // 제휴처 상품 리스트
    @OneToMany(mappedBy = "affiliate")
    private List<VoucherItem> items = new ArrayList<>();

    @Builder
    public VoucherAffiliate(String name, String walletAddress, String walletPrivateKey) {
        this.name = name;
        this.walletAddress = walletAddress;
        this.walletPrivateKey = walletPrivateKey;
    }

    public void updateName(String newName) { this.name = newName; }


}
