package com.ssafy.nhdream.entity.user;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import com.ssafy.nhdream.entity.loan.LoanApproval;
import com.ssafy.nhdream.entity.loan.LoanOptions;
import com.ssafy.nhdream.entity.redeposit.ReDepositAccount;
import com.ssafy.nhdream.entity.saving.SavingAccount;
import com.ssafy.nhdream.entity.transfer.AutomaticTransferTask;
import com.ssafy.nhdream.entity.voucher.VoucherItemTransaction;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    // 식별 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 로그인 아이디
    @Column(name = "user_loginid", nullable = false, unique = true)
    private String loginId;

    // 비밀번호
    @Column(name = "user_password", nullable = false)
    private String password;

    // 이름
    @Column(name = "user_name", nullable = false)
    private String name;

    // 이메일
    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    // 전화번호
    @Column(name = "user_phone", nullable = false)
    private String phone;

    // 생년월일
    @Column(name = "user_birth", nullable = false)
    private LocalDate birth;

    // 회원 상태
    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;

    // 지갑
    @Embedded
    private Wallet wallet;

    // 지갑 비밀번호
    @Column(name = "user_wallet_password", nullable = false)
    private String walletPassword;

    //가입한 적금 상품 리스트
    @OneToMany(mappedBy = "user")
    private List<SavingAccount> savingAccounts = new ArrayList<>();

    // 가입한 예금 상품 리스트
    @OneToMany(mappedBy = "user")
    private List<ReDepositAccount> reDepositAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<VoucherItemTransaction> voucherItemTransactions = new ArrayList<>();

    // 대출 옵션
    @OneToMany(mappedBy = "user")
    private List<LoanOptions> loanOptions = new ArrayList<>();

    // 대출 심사
    @OneToMany(mappedBy = "user")
    private List<LoanApproval> loanApprovals = new ArrayList<>();

    //자동이체내역
    @OneToMany(mappedBy = "user")
    private List<AutomaticTransferTask> automaticTransferTasks = new ArrayList<>();

    // 생성자
    @Builder
    public User(int id, String loginId, String password, String name, String email, String phone, LocalDate birth, UserType type, String walletPassword) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birth = birth;
        this.type = type;
        this.walletPassword = walletPassword;
    }

    public void updatePassword(String password) {
        this.password = password;
    }


    public void updateType(UserType userType) {
        this.type = userType;
    }

    public void updateWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public void updateWalletPassword(String walletPassword) {
        this.walletPassword = walletPassword;
    }

}
