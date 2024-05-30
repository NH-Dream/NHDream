package com.ssafy.nhdream.domain.voucher.service;

import com.ssafy.nhdream.domain.voucher.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VoucherService {
    void createAffiliate(CreateAffiliateReqDto createAffiliateDto);

    void modifyAffiliate(ModifyAffiliateReqDto modifyAffiliateDto);

    void deleteAffiliate(DeleteAffiliateReqDto deleteAffiliateDto);

    List<AffiliateListResDto> getAffiliateList();

    void createItem(CreateVoucherItemReqDto createVoucherItemReqDto, MultipartFile itemImage);

    void modifyItem(ModifyVoucherItemReqDto modifyVoucherItemReqDto);

    void modifyItemImage(ModifyVoucherItemImageReqDto modifyVoucherItemImageReqDto, MultipartFile itemImage);

    void deleteItem(DeleteVoucherItemReqDto deleteVoucherItemReqDto);

    List<VoucherItemResDto> getVoucherItemList();

    List<VoucherItemResDto> getVoucherItemListByCategory(int type);

    VoucherItemResDto getVoucherItem(int itemId);

    VoucherTokenResDto getVoucherToken(String loginId);

    void buyVoucherItem(BuyVoucherItemReqDto buyVoucherItemReqDto);

    VoucherAdminResDto getVoucherDRDCByDate(LocalDate fromDate, LocalDate toDate);

}
