package com.ssafy.nhdream.domain.voucher.repository;

import com.ssafy.nhdream.entity.voucher.VoucherItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherItemRepository extends JpaRepository<VoucherItem, Integer> {

}
