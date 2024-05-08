package com.ssafy.nhdream.domain.user.repository;

import com.ssafy.nhdream.entity.user.Certified;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertifiedRepository extends JpaRepository<Certified, Integer> {

    // 사업자등록번호로 조회
    Optional<Certified> findCertifiedByLicenseNum(long licenseNum);
}
