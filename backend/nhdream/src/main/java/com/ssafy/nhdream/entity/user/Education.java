package com.ssafy.nhdream.entity.user;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "education")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Education extends BaseTimeEntity {

    // 식별 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 교육명
    @Column(name = "edu_name", nullable = false)
    private String name;

    // 교육기관
    @Column(name = "edu_institution", nullable = false)
    private String institution;

    // 기수
    @Column(name = "edu_ordinal", nullable = false)
    private int ordianl;

    // 인증 상태
    @Column(name = "edu_approval", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approval;

    // 첨부파일 이미지
    @Column(name = "edu_image_url", nullable = false)
    private String imageUrl;

    // 회원
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Education(String name, String institution, int ordianl, ApprovalStatus approval, String imageUrl, User user) {
        this.name = name;
        this.institution = institution;
        this.ordianl = ordianl;
        this.approval = approval;
        this.imageUrl = imageUrl;
        this.user = user;
    }

    // 수정 메서드
    public void updateApproval(ApprovalStatus approvalStatus) {
        this.approval = approvalStatus;
    }
}
