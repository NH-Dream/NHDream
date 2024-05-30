package com.ssafy.nhdream.domain.admin.service;

import com.ssafy.nhdream.common.drdc.DRDCService;
import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.common.nhdc.NHDCService;
import com.ssafy.nhdream.common.utils.CalculateDate;
import com.ssafy.nhdream.common.utils.CreateAccountNum;
import com.ssafy.nhdream.domain.admin.dto.*;
import com.ssafy.nhdream.domain.admin.repository.NhTokenFlowRateRepository;
import com.ssafy.nhdream.domain.admin.repository.NhTokenTradingVolumeRepository;
import com.ssafy.nhdream.domain.admin.repository.TaskLogRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositAccountRepository;
import com.ssafy.nhdream.domain.loan.repository.LoanApprovalRepository;
import com.ssafy.nhdream.domain.loan.service.LoanService;
import com.ssafy.nhdream.domain.user.repository.CertifiedRepository;
import com.ssafy.nhdream.domain.user.repository.EducationRepository;
import com.ssafy.nhdream.domain.user.repository.UserRepository;
import com.ssafy.nhdream.entity.admin.NhTokenFlowRate;
import com.ssafy.nhdream.entity.admin.NhTokenTradingVolume;
import com.ssafy.nhdream.entity.admin.TaskLog;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.loan.LoanApproval;
import com.ssafy.nhdream.entity.loan.LoanOptions;
import com.ssafy.nhdream.entity.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private final CertifiedRepository certifiedRepository;
    private final EducationRepository educationRepository;
    private final LoanApprovalRepository loanApprovalRepository;
    private final CreateAccountNum createAccountNum;
    private final FrDepositAccountRepository frDepositAccountRepository;
    private final LoanService loanService;
    private final NHDCService nhdcService;
    private final DRDCService drdcService;
    private final NhTokenFlowRateRepository nhTokenFlowRateRepository;
    private final NhTokenTradingVolumeRepository nhTokenTradingVolumeRepository;
    private final TaskLogRepository taskLogRepository;

    @Value("${NHDC.privateKey}")
    private String serverPrivateKey;

    @Value("${NHDC.contractAddress}")
    private String NHDCContractAddress;

    @Value("${NHDC.interestWallet}")
    private String interestWalletAddress;

    @Override
    @Transactional
    public void reviewTraning(ReviewReqDto reviewReqDto) {

        // 유저 엔티티 조회
        User applicant = userRepository.findById(reviewReqDto.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        // 신청 엔티티 조회
        Education education = educationRepository.findById(reviewReqDto.getApplyPostId())
                .orElseThrow(() -> new CustomException(ExceptionType.TRAINING_NOT_EXIST));

        // 이미 처리를 한 신청이라면 예외처리
        if(!(applicant.getType() == UserType.UNVERIFIED) || !(education.getApproval() == ApprovalStatus.PENDING))
            throw new CustomException(ExceptionType.REVIEW_ALREADY_DONE);

        // 반려시
        if(reviewReqDto.getResult() == 0) {
            applicant.updateType(UserType.REJECTED);
            education.updateApproval(ApprovalStatus.REJECTED);
            return;
        }

        // 지갑생성
        try {
            Credentials credentials = Credentials.create(Keys.createEcKeyPair());
            Wallet wallet = Wallet.builder()
                    .walletAddress(credentials.getAddress())
                    .walletPrivateKey(credentials.getEcKeyPair().getPrivateKey().toString(16))
                    .build();
            applicant.updateWallet(wallet);

        } catch (Exception e) {
            log.error("Failed to create wallet for userID {}: {}",applicant.getId(),e.getMessage(),e);
            throw new CustomException(ExceptionType.FAILED_TO_CREATE_WALLET);
        }

        applicant.updateType(UserType.TRAINEE);
        education.updateApproval(ApprovalStatus.APPROVED);
        
        FrDepositAccount newFrDepositAccount = FrDepositAccount.builder()
            .user(applicant)
            .contractAddress(applicant.getWallet().getWalletAddress())
            .accountNum(createAccountNum.getUsableAccountNum(4))
            .balance(BigDecimal.valueOf(100000000))
            .build();

        frDepositAccountRepository.save(newFrDepositAccount);

        //초기 토큰지급
        try {
            nhdcService.mint(applicant.getWallet().getWalletAddress(), BigInteger.valueOf(100000000), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()), serverPrivateKey);
        } catch (Exception e) {
            log.error("초기자금 지급 실패 userID: {} 오류메시지 : {}", applicant.getId(), e.getMessage(), e);
            throw new CustomException(ExceptionType.FAILTE_TO_FIRESTTOKEN);
        }

        //초기 드림토큰지급
        try {
            drdcService.mint(applicant.getWallet().getWalletAddress(), BigInteger.valueOf(1000000), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()), serverPrivateKey);
        } catch (Exception e) {
            log.error("초기드림토큰자금 지급 실패 userID: {} 오류메시지 : {}", applicant.getId(), e.getMessage(), e);
            throw new CustomException(ExceptionType.FAILTE_TO_FIRESTTOKEN);
        }

    }

    @Override
    @Transactional
    public void reviewFarmer(ReviewReqDto reviewReqDto) {

        // 유저 엔티티 조회
        User applicant = userRepository.findById(reviewReqDto.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        // 신청 엔티티 조회
        Certified certified = certifiedRepository.findById(reviewReqDto.getApplyPostId())
                .orElseThrow(() -> new CustomException(ExceptionType.CERTIFIED_NOT_EXIST));

        // 이미 처리를 한 신청이라면 예외처리
        if(!(applicant.getType() == UserType.UNVERIFIED) || !(certified.getApproval() == ApprovalStatus.PENDING))
            throw new CustomException(ExceptionType.REVIEW_ALREADY_DONE);

        // 상태 변경
        if(reviewReqDto.getResult() == 0){ // 반려
            applicant.updateType(UserType.REJECTED);
            certified.updateApproval(ApprovalStatus.REJECTED);
        }else{
            applicant.updateType(UserType.FARMER);
            certified.updateApproval(ApprovalStatus.APPROVED);

            //지갑생성
            try {
                Credentials credentials = Credentials.create(Keys.createEcKeyPair());
                Wallet wallet = Wallet.builder()
                        .walletAddress(credentials.getAddress())
                        .walletPrivateKey(credentials.getEcKeyPair().getPrivateKey().toString(16))
                        .build();
                applicant.updateWallet(wallet);

            } catch (Exception e) {
                log.error("Failed to create wallet for userID {}: {}",applicant.getId(),e.getMessage(),e);
                throw new CustomException(ExceptionType.FAILED_TO_CREATE_WALLET);
            }
        }
        return;
    }

    @Override
    @Transactional
    public void reviewLoan(LoanReviewReqDto loanReviewReqDto) {

        // 신청 엔티티 조회
        LoanApproval loanApproval = loanApprovalRepository.findById(loanReviewReqDto.getApplyPostId())
                .orElseThrow(() -> new CustomException(ExceptionType.LOAN_APPLY_NOT_EXIST));

        // 이미 처리를 한 신청이라면 예외처리
        if(!(loanApproval.getApproval() == ApprovalStatus.PENDING))
            throw new CustomException(ExceptionType.REVIEW_ALREADY_DONE);

        // 상태 변경
        if(loanReviewReqDto.getResult() == 0){
            // 반려
            loanApproval.updateApproval(ApprovalStatus.REJECTED);
        }else{
            // 승인
            loanApproval.updateApproval(ApprovalStatus.APPROVED);

            /*
            * 여기에 대출 계좌 생성 및 추가 로직 작성하시면 됩니다.
            * */
            loanService.createLoanAccount(loanApproval.getId());

        }
        return;
    }

    @Override
    public LoanReviewResDto getLoanReviewList(String status, String userName, Pageable pageable) {

        Page<LoanReviewContentDto> loanApprovals = loanApprovalRepository.getLoanReviewList(status, userName, pageable);

        int curCnt = loanApprovals.getNumber() * 10 + 1;
        for (LoanReviewContentDto loanReviewResDto : loanApprovals.getContent()) {
            loanReviewResDto.setCurReviewCnt(curCnt++);
        }

        LoanReviewResDto loanReviewResDto = new LoanReviewResDto(loanApprovals.getTotalPages(), loanApprovals.getContent());

        return loanReviewResDto;

    }

    @Override
    public LoanReviewDetailResDto getLoanReviewDetail(int loanReviewId) {

        LoanApproval loanApproval = loanApprovalRepository.findById(loanReviewId).orElseThrow(() -> new CustomException(ExceptionType.LOAN_APPLY_NOT_EXIST));

        LoanOptions loanOptions = loanApproval.getLoanOptions();

        BigDecimal principal = loanApproval.getLoanOptions().getAmount();
        BigDecimal rate = loanApproval.getLoanOptions().getRate();


        // 이자 계산 = 원금 * 이자율 / 100 -> 소수점 내림
        BigDecimal interest = principal.multiply(rate).divide(BigDecimal.valueOf(100), 0, RoundingMode.DOWN);

        BigDecimal payment = principal.add(interest);

        User user = loanApproval.getUser();

        FrDepositAccount frDepositAccount = frDepositAccountRepository.findByUser(user).get();

        LoanReviewDetailResDto loanReviewDetailResDto = LoanReviewDetailResDto.builder()
                .userName(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .birthDate(user.getBirth())
                .userType((user.getType() == UserType.FARMER)? "농부교육수료자" : "귀농인")
                .nhAmount(frDepositAccount.getBalance().toString())
                .productName(loanApproval.getLoanOptions().getLoanProduct().getName())
                .loanAmount(loanApproval.getLoanOptions().getAmount())
                .paymentMethod("원리금균등분할상환")
                .term(loanApproval.getLoanOptions().getTerm())
                .rate(loanApproval.getLoanOptions().getRate())
                .totalPayments(payment.toString())
                .copyOfIdCard(loanApproval.getIdentityUrl())
                .farmlandCert(loanApproval.getFarmlandAccessUrl())
                .incomeCert(loanApproval.getIncomeCertificateUrl())
                .nationalTaxCert(loanApproval.getNationalTaxCertificateUrl())
                .localTaxCert(loanApproval.getLocalTaxCertificateUrl())
                .build();

        return loanReviewDetailResDto;
    }

    @Override
    public List<NhTokenFlowDetailResDto> getNhTokenFlowResList(LocalDate startDate, LocalDate endDate) {
        List<NhTokenFlowRate> nhTokenFlowRateList = nhTokenFlowRateRepository.findNhTokenFlowRateListByDates(startDate, endDate
        );
        return nhTokenFlowRateList.stream().map(nhTokenFlowRate -> NhTokenFlowDetailResDto.builder()
                .id(nhTokenFlowRate.getId())
                .mintAmount(nhTokenFlowRate.getMintAmount())
                .burnAmount(nhTokenFlowRate.getBurnAmount())
                .referenceDate(nhTokenFlowRate.getReferenceDate())
                .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<NhTokenTradingVolumeDetailResDto> getNhTokenTradeFlow(LocalDate startDate, LocalDate endDate) {
        List<NhTokenTradingVolume> nhTokenTradingVolumeList = nhTokenTradingVolumeRepository.findNhTokenTradingVolumeByDates(startDate, endDate);

        return nhTokenTradingVolumeList.stream().map(nhTokenTradingVolume -> NhTokenTradingVolumeDetailResDto.builder()
                .id(nhTokenTradingVolume.getId())
                .tradeAmount(nhTokenTradingVolume.getTradeAmount())
                .referenceDate(nhTokenTradingVolume.getReferenceDate())
                .build())
                .collect(Collectors.toList());
    }

    @Override
    public PersonalWalletCountResDto getPersonalWalletCount() {
        int walletCount = userRepository.countUserWithWallet();
        return PersonalWalletCountResDto.builder()
                .count(walletCount)
                .build();

    }

    @Override
    public TrainingReviewResDto getTrainingReviewList(String userName, String status, Pageable pageable) {

        Page<TrainingReviewContentDto> trainingReviewList = educationRepository.getTrainingReviewList(userName, status, pageable);

        int curCnt = trainingReviewList.getNumber() * 10 + 1;
        for (TrainingReviewContentDto trainingReviewContentDto : trainingReviewList.getContent()) {
            trainingReviewContentDto.setCurReviewCnt(curCnt++);
        }

        return new TrainingReviewResDto(trainingReviewList.getTotalPages(), trainingReviewList.getContent());

    }

    @Override
    public FarmerReviewResDto getFarmerReviewList(String userName, Pageable pageable) {

        Page<FarmerReviewContentDto> farmerReviewList = certifiedRepository.getFarmerReviewList(userName, pageable);

        int curCnt = farmerReviewList.getNumber() * 10 + 1;
        for (FarmerReviewContentDto farmerReviewContentDto : farmerReviewList.getContent()) {
            farmerReviewContentDto.setCurReviewCnt(curCnt++);
        }

        return new FarmerReviewResDto(farmerReviewList.getTotalPages(), farmerReviewList.getContent());
    }

    @Override
    public FarmerReviewDetailResDto getFarmerReviewDetail(int farmerReviewId) {

        Certified certified = certifiedRepository.findById(farmerReviewId).orElseThrow(() -> new CustomException(ExceptionType.CERTIFIED_NOT_EXIST));
        User user = certified.getUser();

        return FarmerReviewDetailResDto.builder()
                .userName(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .birthDate(user.getBirth())
                .representative(certified.getRepresentative())
                .openDate(certified.getOpenDate())
                .licenseNum(certified.getLicenseNum())
                .businessLicense(certified.getImageUrl())
                .build();

    }

    @Override
    public TrainingReviewDetailResDto getTrainingReviewDetail(int trainingReviewId) {
        Education education = educationRepository.findById(trainingReviewId).orElseThrow(() -> new CustomException(ExceptionType.EDUCATION_NOT_EXIST));
        User user = education.getUser();

        return TrainingReviewDetailResDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .birthDate(user.getBirth())
                .trainingName(education.getName())
                .trainingInstitution(education.getInstitution())
                .ordinal(education.getOrdianl())
                .certificate(education.getImageUrl())
                .build();
    }

    //작업로그리스트조회
    @Override
    public List<LogListResDto> getLogList() {
        List<TaskLog> taskLogs = taskLogRepository.failedTaskLogList();
        return taskLogs.stream().map(log -> LogListResDto.builder()
                .id(log.getId())
                .isSuccess(log.isSuccess())
                .isProcessed(log.isProcessed())
                .errorType(log.getErrorType())
                .errorMessage(log.getErrorMessage())
                .jopType(log.getJobType())
                .build())
                .collect(Collectors.toList());
    }


    //작업로그상세조회
    public LogDetailResDto getLogDetail(int logId) {
        TaskLog log = taskLogRepository.findById(logId).orElseThrow(() -> new CustomException(ExceptionType.TASKLOG_NOT_EXIST));
        return LogDetailResDto.builder()
                .id(log.getId())
                .isSuccess(log.isSuccess())
                .isProcessed(log.isProcessed())
                .errorType(log.getErrorType())
                .errorMessage(log.getErrorMessage())
                .jopType(log.getJobType())
                .errorDetail(log.getErrorDetail())
                .build();
    }
}
