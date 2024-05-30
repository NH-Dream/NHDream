package com.ssafy.nhdream.entity.admin;

import com.ssafy.nhdream.entity.baseentity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "admin")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseTimeEntity {

    // 식별 ID
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 관리자 아이디
    @Column(name = "admin_loginId", nullable = false, unique = true)
    private String loginId;

    // 관리자 비밀번호
    @Column(name = "admin_pw", nullable = false)
    private String pw;

    // 관리자 부서
    @Column(name = "admin_dept", nullable = false)
    private String dept;

    // 2차 비밀번호
    @Column(name = "admin_sec_pw", nullable = false)
    private String secPw;

    // 마지막 접속기록
    @Column(name = "admin_last_log")
    private LocalDateTime lastLog;

    // 관리자 접속 기록 매핑
    @OneToMany(mappedBy = "admin")
    private List<AdminLoginLog> adminLoginLogs = new ArrayList<>();

    // 생성자
    @Builder
    public Admin(String loginId, String pw, String dept, String secPw, LocalDateTime lastLog) {
        this.loginId = loginId;
        this.pw = pw;
        this.dept = dept;
        this.secPw = secPw;
        this.lastLog = lastLog;
    }

    // 수정 메서드
    public void updatedDept(String dept){this.dept = dept;};

    public void updatedLastLog(String dept){this.dept = dept;};

}
