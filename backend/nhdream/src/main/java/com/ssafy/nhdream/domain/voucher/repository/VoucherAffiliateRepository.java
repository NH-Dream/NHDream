package com.ssafy.nhdream.domain.voucher.repository;

import com.ssafy.nhdream.domain.voucher.dto.AffiliateListResDto;
import com.ssafy.nhdream.entity.voucher.VoucherAffiliate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherAffiliateRepository extends JpaRepository<VoucherAffiliate, Integer> {
    Optional<VoucherAffiliate> findAffiliateById(Integer id);

    @Query ("Select new com.ssafy.nhdream.domain.voucher.dto.AffiliateListResDto(AF.name, AF.walletAddress, AF.walletPrivateKey, AF.createdAt) " +
            "from VoucherAffiliate AF " +
            "where 1 = 1")
    List<AffiliateListResDto> findAllAffiliates();
}
