package com.ssafy.nhdream.entity.admin;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "admin_loginlog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminLoginLog extends BaseTimeEntity {

    // 식별 ID
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 관리자 식별 FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    // 생성자
    @Builder
    public AdminLoginLog(Admin admin) {
        this.admin = admin;
    }
}
