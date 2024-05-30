package com.ssafy.nhdream.domain.voucher.service;

import com.ssafy.nhdream.common.drdc.DRDCContract;
import com.ssafy.nhdream.common.drdc.DRDCService;
import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.common.nhdc.NHDCContract;
import com.ssafy.nhdream.common.nhdc.NHDCService;
import com.ssafy.nhdream.common.s3.AwsS3Service;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositAccountRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositTransactionRepository;
import com.ssafy.nhdream.domain.frdeposit.service.FrDepositService;
import com.ssafy.nhdream.domain.sse.dto.NotificationType;
import com.ssafy.nhdream.domain.sse.service.NotificationService;
import com.ssafy.nhdream.domain.user.repository.UserRepository;
import com.ssafy.nhdream.domain.user.service.UserService;
import com.ssafy.nhdream.domain.voucher.dto.*;
import com.ssafy.nhdream.domain.voucher.repository.VoucherAffiliateRepository;
import com.ssafy.nhdream.domain.voucher.repository.VoucherItemRepository;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.frdeposit.FrDepositTransaction;
import com.ssafy.nhdream.entity.user.User;
import com.ssafy.nhdream.entity.user.Wallet;
import com.ssafy.nhdream.entity.voucher.VoucherAffiliate;
import com.ssafy.nhdream.entity.voucher.VoucherItem;
import io.reactivex.Flowable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherServiceImpl implements VoucherService {


    @Value("${DRDC.contractAddress}")
    private String contractAddress;

    @Value("${NHDC.privateKey}")
    private String contractPrivateKey;

    private final VoucherAffiliateRepository voucherAffiliateRepository;
    private final VoucherItemRepository voucherItemRepository;
    private final DRDCService drdcService;
    private final NHDCService nhdcService;
    private final FrDepositAccountRepository frDepositAccountRepository;
    private final FrDepositTransactionRepository frDepositTransactionRepository;

    private final AwsS3Service awsS3Service;
    private final UserRepository userRepository;

    // sse 설정
    private final NotificationService notificationService;

    // 바우처 생성
    @Override
    @Transactional
    public void createAffiliate(CreateAffiliateReqDto createAffiliateReqDto) {

        String walletAddress;
        String walletPrivateKey;

        try {
            Credentials credentials = Credentials.create(Keys.createEcKeyPair());

            walletAddress = credentials.getAddress();
            walletPrivateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);

        } catch (Exception e) {
            log.error("Failed to create wallet: {}", e);
            throw new CustomException(ExceptionType.FAILED_TO_CREATE_WALLET);
        }

        // 임시로 AccountNum, BigDecimal 넣어둠
        VoucherAffiliate affiliate = VoucherAffiliate.builder()
                .name(createAffiliateReqDto.getName())
                .walletAddress(walletAddress)
                .walletPrivateKey(walletPrivateKey)
                .build();

        voucherAffiliateRepository.save(affiliate);
    }

    // 바우처 수정
    @Override
    @Transactional
    public void modifyAffiliate(ModifyAffiliateReqDto modifyAffiliateReqDto) {

        VoucherAffiliate affiliate = voucherAffiliateRepository.findAffiliateById(modifyAffiliateReqDto.getId()).orElseThrow(()->new CustomException(ExceptionType.VOUCHER_NOT_EXIST));

        affiliate.updateName(modifyAffiliateReqDto.getName());

        voucherAffiliateRepository.save(affiliate);
    }

    // 바우처 삭제
    @Override
    @Transactional
    public void deleteAffiliate(DeleteAffiliateReqDto deleteAffiliateReqDto) {

        VoucherAffiliate affiliate = voucherAffiliateRepository.findAffiliateById(deleteAffiliateReqDto.getId()).orElseThrow(()->new CustomException(ExceptionType.VOUCHER_NOT_EXIST));

        voucherAffiliateRepository.delete(affiliate);
    }

    // 바우처 전체 리스트 조회
    @Override
    public List<AffiliateListResDto> getAffiliateList() {
        return voucherAffiliateRepository.findAllAffiliates();
    }

    // 바우처 상품 생성
    @Override
    @Transactional
    public void createItem(CreateVoucherItemReqDto createVoucherItemReqDto, MultipartFile itemImage) {

        String imageUrl = awsS3Service.uploadFile(itemImage);

        log.info("itemImageUrl : {}", imageUrl);

        VoucherAffiliate voucherAffiliate = voucherAffiliateRepository.findAffiliateById(createVoucherItemReqDto.getAffiliateId()).orElseThrow(() -> new CustomException(ExceptionType.VOUCHER_NOT_EXIST));

        VoucherItem item = VoucherItem.builder()
                .title(createVoucherItemReqDto.getName())
                .price(createVoucherItemReqDto.getPrice())
                .quantity(createVoucherItemReqDto.getQuantity())
                .image(imageUrl)
                .type(createVoucherItemReqDto.getType())
                .affiliate(voucherAffiliate)
                .build();

        voucherItemRepository.save(item);
    }

    // 바우처 상품 수정
    @Override
    @Transactional
    public void modifyItem(ModifyVoucherItemReqDto modifyVoucherItemReqDto) {

        VoucherItem item = voucherItemRepository.findById(modifyVoucherItemReqDto.getId())
                .orElseThrow(() -> new CustomException(ExceptionType.VOUCHER_ITEM_NOT_EXIST));

        item.updateItem(modifyVoucherItemReqDto.getName(), modifyVoucherItemReqDto.getPrice(), modifyVoucherItemReqDto.getQuantity(), modifyVoucherItemReqDto.getType());

        voucherItemRepository.save(item);
    }

    // 바우처 상품 이미지 수정
    @Override
    @Transactional
    public void modifyItemImage(ModifyVoucherItemImageReqDto modifyVoucherItemImageReqDto, MultipartFile itemImage) {

        String imageUrl = awsS3Service.uploadFile(itemImage);

        log.info("itemImageUrl : {}", imageUrl);

        VoucherItem item = voucherItemRepository.findById(modifyVoucherItemImageReqDto.getId())
                .orElseThrow(() -> new CustomException(ExceptionType.VOUCHER_ITEM_NOT_EXIST));

        item.updateItemImage(imageUrl);

        voucherItemRepository.save(item);
    }

    // 바우처 상품 삭제
    @Override
    @Transactional
    public void deleteItem(DeleteVoucherItemReqDto deleteVoucherItemReqDto) {

        VoucherItem voucherItem = voucherItemRepository.findById(deleteVoucherItemReqDto.getId())
                .orElseThrow(() -> new CustomException(ExceptionType.VOUCHER_ITEM_NOT_EXIST));

        voucherItemRepository.delete(voucherItem);
    }

    // 바우처 상품 전체 리스트 조회
    @Override
    public List<VoucherItemResDto> getVoucherItemList() {

        return voucherItemRepository.findAllVoucherItemsList();
    }

    // 바우처 카테고리별 아이템 조회
    @Override
    public List<VoucherItemResDto> getVoucherItemListByCategory(int type) {

        return voucherItemRepository.findAllVoucherItemsListByCategory(type);
    }

    // 바우처 상품 상세 조회
    @Override
    public VoucherItemResDto getVoucherItem(int itemId) {

        return voucherItemRepository.findVoucherItems(itemId);
    }

    // 바우처 토큰량 조회
    @Override
    public VoucherTokenResDto getVoucherToken(String loginId) {

        // 지갑 주소로 drdc 불러오는 부분
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        Wallet userWallet = user.getWallet();

        String addr = userWallet.getWalletAddress();
        String privateKey = userWallet.getWalletPrivateKey();

        BigInteger balance = new BigInteger("0");
        BigInteger allMinted = new BigInteger("0");

        try {
            balance = drdcService.getBalance(addr, privateKey);
            log.info("{}", balance);

            DRDCContract contract = drdcService.loadContract(privateKey);

            RemoteFunctionCall<BigInteger> bigIntegerRemoteFunctionCall = contract.totalMinted();

            log.info("{}", bigIntegerRemoteFunctionCall.send());

        } catch (Exception e) {
            log.error("getVoucherToken Error : {}", e);
        }

        return new VoucherTokenResDto(balance);
    }

    @Override
    @Transactional
    public void buyVoucherItem(BuyVoucherItemReqDto buyVoucherItemReqDto) {
        // itemId로 바우처 불러오기
        VoucherItem voucherItem = voucherItemRepository.findById(buyVoucherItemReqDto.getItemId())
                .orElseThrow(() -> new CustomException(ExceptionType.VOUCHER_ITEM_NOT_EXIST));

        // 바우처 정보 불러오기
        VoucherAffiliate affiliate = voucherItem.getAffiliate();

        String affiliateAddr = affiliate.getWalletAddress();
        String affiliatePrivateKey = affiliate.getWalletPrivateKey();

        // loginId로 사용자 정보 불러오기
        User curUser = userRepository.findByLoginId(buyVoucherItemReqDto.getLoginId()).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        // 사용자 지갑 정보 불러오기
        Wallet userWallet = curUser.getWallet();

        String userAddr = userWallet.getWalletAddress();
        String userPrivateKey = userWallet.getWalletPrivateKey();

        // 보내는 사람 트랜잭션 발생용
        FrDepositAccount senderFrAccount = frDepositAccountRepository.findFrDepositAccountByContractAddress(userAddr)
                .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));

        BigInteger nhdcBalance = new BigInteger("0");
        BigInteger drdcBalance = new BigInteger("0");

        // user의 지갑에서 nhdc 토큰량, drdc 토큰량 불러와야 함.
        try {
            nhdcBalance = nhdcService.getBalance(userAddr, userPrivateKey);
            drdcBalance = drdcService.getBalance(userAddr, userPrivateKey);
        } catch (Exception e) {
            log.error("buy Item Error : nh : {} , dr : {}" , nhdcBalance, drdcBalance);
        }

        BigInteger usingNHDCBigInteger = BigInteger.valueOf(buyVoucherItemReqDto.getUsingNHDC());
        BigInteger usingDRDCBigInteger = BigInteger.valueOf(buyVoucherItemReqDto.getUsingDRDC());

        // nhdc , drdc 부족하면 예외처리 하기
        // nhdc 사용량이 남은 잔고보다 더 많으면 에러 처리
        if (usingNHDCBigInteger.compareTo(nhdcBalance) > 0) {
            throw new CustomException(ExceptionType.VOUCHER_ITEM_NHDC_NOT_ENOUGH);
        }
        // drdc 사용량이 남은 잔고보다 더 많으면 에러 처리
        if (usingDRDCBigInteger.compareTo(drdcBalance) > 0) {
            throw new CustomException(ExceptionType.VOUCHER_ITEM_DRDC_NOT_ENOUGH);
        }

        // 형 변환
        BigDecimal transferNHDC = new BigDecimal(buyVoucherItemReqDto.getUsingNHDC());
        BigDecimal transferDRDC = new BigDecimal(buyVoucherItemReqDto.getUsingDRDC());

        // 유저 지갑에서 바우처 지갑으로 돈 전송 해주기
        try {
            nhdcService.transfer(affiliateAddr, transferNHDC, userPrivateKey);
            drdcService.transfer(affiliateAddr, transferDRDC, userPrivateKey);

            senderFrAccount.transferDecreaseFrDepositBalance(transferNHDC);

            BigInteger remainingInteger = nhdcService.getBalance(userAddr, userPrivateKey);

            BigDecimal remainingDecimal = new BigDecimal(remainingInteger);

            String transactionHash = "test";

            // nhdc 사용량이 0원일 경우 트랜잭션 발생시키지 않기
            if (transferNHDC.intValue() != 0) {

                //보내는 쪽의 거래내역
                FrDepositTransaction senderTransaction = FrDepositTransaction.builder()
                        .frDepositAccount(senderFrAccount)
                        .type(1)
                        .oppositeAccount(affiliateAddr)
                        .oppositeName(affiliate.getName())
                        .tradeAmount(transferNHDC)
                        .remainingBalance(remainingDecimal)
                        .transactionHash(transactionHash)
                        .build();

                frDepositTransactionRepository.save(senderTransaction);
            }

            // 바우처 트랜잭션 발생 시켜야 하나?
            // drdc 사용량이 0원일 경우 트랜잭션 발생시키지 않기

        } catch (Exception e) {
            log.error("transfer error occurred");
            throw new CustomException(ExceptionType.VOUCHER_ITEM_BUY_ERROR);
        }

        // 구매 수량 만큼 재고에서 빼고 잔고에서 빼주기
        if(voucherItem.getQuantity() > buyVoucherItemReqDto.getAmount()) {
            voucherItem.substractQuantity(buyVoucherItemReqDto.getAmount());
        } else {
            throw new CustomException(ExceptionType.VOUCHER_ITEM_NOT_ENOUGH);
        }

        // sse 설정
        notificationService.saveNotice(NotificationType.DREAM_EVENT, curUser.getId(), BigDecimal.valueOf(buyVoucherItemReqDto.getUsingNHDC()), 1, 0, voucherItem.getAffiliate().getName());
    }

    @Override
    public VoucherAdminResDto getVoucherDRDCByDate(LocalDate fromDate, LocalDate toDate) {
        log.info("===== getVoucherDRDCByDate Start =====");

        List<BigInteger> mintedList = drdcService.getMintedEventFlowable(fromDate, toDate);
        List<BigInteger> burnedlist = drdcService.getBurnedEventFlowable(fromDate, toDate);

        return new VoucherAdminResDto(mintedList, burnedlist);
    }
}
