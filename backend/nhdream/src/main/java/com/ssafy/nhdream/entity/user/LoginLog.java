package com.ssafy.nhdream.entity.user;

import com.ssafy.nhdream.entity.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_loginlog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginLog extends BaseEntity {

    // 식별 ID
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public LoginLog(User user) {
        this.user = user;
    }
}
