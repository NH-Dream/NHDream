package com.ssafy.nhdream.domain.voucher.repository;

import com.ssafy.nhdream.domain.voucher.dto.VoucherItemResDto;
import com.ssafy.nhdream.entity.voucher.VoucherItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherItemRepository extends JpaRepository<VoucherItem, Integer> {

    @Query("Select new com.ssafy.nhdream.domain.voucher.dto.VoucherItemResDto(VI.id, VI.title, VI.createdAt, VI.price, VI.quantity, VI.image, VI.type, VI.affiliate.name) " +
            "From VoucherItem VI " +
            "Where 1 = 1 ")
    List<VoucherItemResDto> findAllVoucherItemsList();

    @Query("Select new com.ssafy.nhdream.domain.voucher.dto.VoucherItemResDto(VI.id, VI.title, VI.createdAt, VI.price, VI.quantity, VI.image, VI.type, VI.affiliate.name) " +
            "From VoucherItem VI " +
            "Where 1 = 1 and " +
            "VI.id = :itemId "
    )
    VoucherItemResDto findVoucherItems(int itemId);

    @Query("Select new com.ssafy.nhdream.domain.voucher.dto.VoucherItemResDto(VI.id, VI.title, VI.createdAt, VI.price, VI.quantity, VI.image, VI.type, VI.affiliate.name) " +
            "From VoucherItem VI " +
            "Where 1 = 1 and " +
            "VI.type = :type ")
    List<VoucherItemResDto> findAllVoucherItemsListByCategory(int type);
}
