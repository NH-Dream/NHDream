package com.ssafy.nhdream.entity.user;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "certified")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certified extends BaseTimeEntity {

    // 식별 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 대표자명
    @Column(name = "cert_representative", nullable = false)
    private String representative;

    // 사업 일자
    @Column(name = "cert_date", nullable = false)
    private LocalDate openDate;

    // 사업자 번호
    @Column(name = "cert_licenseNum", nullable = false)
    private long licenseNum;

    // 사업자 이미지
    @Column(name = "cert_image_url", nullable = false)
    private String imageUrl;

    // 인증 상태
    @Column(name = "cert_approval", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approval;

    // 회원
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Certified(String representative, LocalDate openDate, long licenseNum, String imageUrl, ApprovalStatus approval, User user) {
        this.representative = representative;
        this.openDate = openDate;
        this.licenseNum = licenseNum;
        this.imageUrl = imageUrl;
        this.approval = approval;
        this.user = user;
    }

    // 수정 메서드
    public void updateApproval(ApprovalStatus approvalStatus) {
        this.approval = approvalStatus;
    }
}
