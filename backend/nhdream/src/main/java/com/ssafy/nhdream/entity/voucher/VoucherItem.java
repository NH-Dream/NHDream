package com.ssafy.nhdream.entity.voucher;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "voucher_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoucherItem extends BaseTimeEntity {

    // ID
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 바우처 상품 제목
    @Column(name = "item_title", nullable = false)
    private String title;

    // 바우처 상품 가격
    @Column(name = "item_price", nullable = false)
    private BigDecimal price;

    // 바우처 상품 수량
    @Column(name = "item_quantity", nullable = false)
    private int quantity;

    // 바우처 상품 이미지
    @Column(name = "item_image", nullable = false)
    private String image;

    @Column(name = "item_type", nullable = false)
    private int type;

    // 제휴처 pk 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_id")
    private VoucherAffiliate affiliate;

    // 제휴처 상품 리스트
    @OneToMany(mappedBy = "voucherItem")
    private List<VoucherItemTransaction> voucherItemTransactions = new ArrayList<>();

    @Builder
    public VoucherItem(String title, BigDecimal price, int quantity, String image, int type, VoucherAffiliate affiliate) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.type = type;
        this.affiliate = affiliate;
    }

    public void substractQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public void updateItem(String title, BigDecimal price, int quantity, int type) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    public void updateItemImage(String image) {
        this.image = image;
    }


}
