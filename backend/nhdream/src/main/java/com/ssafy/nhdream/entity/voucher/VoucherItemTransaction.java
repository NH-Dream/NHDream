package com.ssafy.nhdream.entity.voucher;

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
@Table(name = "voucher_item_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoucherItemTransaction extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 농협 토큰 양
    @Column(name = "nht_amount", nullable = false)
    private BigDecimal nhtAmount;

    // 바우처 토큰 양
    @Column(name = "vt_amount", nullable = false)
    private BigDecimal vtAmount;

    // user fk 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // voucher fk 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    private VoucherItem voucherItem;

    @Builder
    public VoucherItemTransaction(BigDecimal nhtAmount, BigDecimal vtAmount) {
        this.nhtAmount = nhtAmount;
        this.vtAmount = vtAmount;
    }

}
