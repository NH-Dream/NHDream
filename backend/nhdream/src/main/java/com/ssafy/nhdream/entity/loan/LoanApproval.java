package com.ssafy.nhdream.entity.loan;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import com.ssafy.nhdream.entity.user.ApprovalStatus;
import com.ssafy.nhdream.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "loan_approval")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanApproval extends BaseTimeEntity {

    // PK
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 대출 심사 상태
    @Column(name = "loan_type", nullable = false)
    private ApprovalStatus approval;

    // 신분증
    @Column(name = "identity_url", nullable = false)
    private String identityUrl;

    // 농지 취득 자격 증명서
    @Column(name = "farmland_access_url", nullable = false)
    private String farmlandAccessUrl;

    // 소득 금액 증명서
    @Column(name = "income_certificate_url", nullable = false)
    private String incomeCertificateUrl;

    // 국세 완납 증명서
    @Column(name = "national_tax_certificate_url", nullable = false)
    private String nationalTaxCertificateUrl;

    // 지방세 완납 증명서
    @Column(name = "local_tax_certificate_url", nullable = false)
    private String localTaxCertificateUrl;



    // FK (회원 PK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // FK (대출 옵션 PK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_option_id", nullable = false)
    private LoanOptions loanOptions;

    // 대출 통장 매핑
    @OneToOne(mappedBy = "loanApproval")
    private LoanAccount loanAccount;


    // 생성자
    @Builder
    public LoanApproval(User user, LoanOptions loanOptions, ApprovalStatus approval, String identityUrl,
                        String farmlandAccessUrl, String incomeCertificateUrl,
                        String nationalTaxCertificateUrl, String localTaxCertificateUrl) {
        this.user = user;
        this.loanOptions = loanOptions;
        this.approval = approval;
        this.identityUrl = identityUrl;
        this.farmlandAccessUrl = farmlandAccessUrl;
        this.incomeCertificateUrl = incomeCertificateUrl;
        this.nationalTaxCertificateUrl = nationalTaxCertificateUrl;
        this.localTaxCertificateUrl = localTaxCertificateUrl;
    }

    // 수정 메서드
    public void updateApproval(ApprovalStatus approvalStatus) {
        this.approval = approvalStatus;
    }

}
