package com.ssafy.nhdream.domain.user.service;

import com.ssafy.nhdream.common.drdc.DRDCService;
import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.common.nhdc.NHDCService;
import com.ssafy.nhdream.common.s3.AwsS3Service;
import com.ssafy.nhdream.common.utils.CalculateDate;
import com.ssafy.nhdream.common.utils.CreateAccountNum;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositAccountRepository;
import com.ssafy.nhdream.domain.redeposit.repository.ReDepositAccountRepository;
import com.ssafy.nhdream.domain.saving.repository.SavingAccountRepository;
import com.ssafy.nhdream.domain.user.dto.*;
import com.ssafy.nhdream.domain.user.openApi.OpenApiManager;
import com.ssafy.nhdream.domain.user.repository.CertifiedRepository;
import com.ssafy.nhdream.domain.user.repository.EducationRepository;
import com.ssafy.nhdream.domain.user.repository.UserRepository;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final EducationRepository educationRepository;
    private final CertifiedRepository certifiedRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final ReDepositAccountRepository reDepositAccountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AwsS3Service awsS3Service;
    private final OpenApiManager openApiManager;
    private final CreateAccountNum createAccountNum;
    private final FrDepositAccountRepository frDepositAccountRepository;
    private final NHDCService nhdcService;
    private final DRDCService drdcService;

    @Value("${NHDC.privateKey}")
    private String serverPrivateKey;

    // 회원가입
    @Override
    @Transactional
    public int join(JoinReqDto joinReqDto) {

        User user = User.builder()
                .loginId(joinReqDto.getLoginId())
                .password(bCryptPasswordEncoder.encode(joinReqDto.getPassword()))
                .name(joinReqDto.getName())
                .email(joinReqDto.getEmail())
                .phone(joinReqDto.getPhone())
                .birth(joinReqDto.getBirthDate())
                .walletPassword(bCryptPasswordEncoder.encode(joinReqDto.getWalletPassword()))
                .type(UserType.UNVERIFIED)
                .build();

        return userRepository.save(user).getId();
    }

    @Override
    public void checkIdDuplicate(String id) {
        // 해당 아이디의 유저가 있다면 예외처리
        if(userRepository.findByLoginId(id).isPresent()) throw new CustomException(ExceptionType.DUPLICATE_LOGIN_ID);

    }

    // 교육 인증 신청
    @Override
    @Transactional
    public void applyEducation(ApplyEducationReqDto applyEducationReqDto, MultipartFile educationImg) {

        // 이미지 저장
        String imgURl = awsS3Service.uploadFile(educationImg);

        // 회원 조회
        User user = userRepository.findByLoginId(applyEducationReqDto.getLoginId()).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        // 엔티티 생성
        Education education =Education.builder()
                .name("청년농부사관학교")
                .institution("NH농협")
                .ordianl(applyEducationReqDto.getOrdinal())
                .approval(ApprovalStatus.PENDING)
                .imageUrl(imgURl)
                .user(user)
                .build();

        // 저장
        educationRepository.save(education);

    }

    @Override
    @Transactional
    public void applyFarmer(ApplyFarmerReqDto applyFarmerReqDto, MultipartFile buisinessImg) {
        // 회원 조회
        User user = userRepository.findByLoginId(applyFarmerReqDto.getLoginId()).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        // 사업자 등록번호 중복 검사
//        if(certifiedRepository.findCertifiedByLicenseNum(applyFarmerReqDto.getLicenseNum()).isPresent()) throw new CustomException(ExceptionType.DUPLICATE_LICENSE_NUM);

        // 사업자 번호 인증
        openApiManager.validateLicense(
                String.valueOf(applyFarmerReqDto.getLicenseNum()),
                applyFarmerReqDto.getRepresentative(),
                applyFarmerReqDto.getRegistrationDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));


        // 이미지 저장
        String imgURl = awsS3Service.uploadFile(buisinessImg);
        // 엔티티 생성
        Certified certified = Certified.builder()
                .representative(applyFarmerReqDto.getRepresentative())
                .openDate(applyFarmerReqDto.getRegistrationDate())
                .licenseNum(applyFarmerReqDto.getLicenseNum())
                .imageUrl(imgURl)
                .user(user)
                .approval(ApprovalStatus.APPROVED)
                .build();

        // 저장
        certifiedRepository.save(certified);

        // 회원 인증 상태 수정
        user.updateType(UserType.FARMER);

        // 지갑생성
        try {
            Credentials credentials = Credentials.create(Keys.createEcKeyPair());
            Wallet wallet = Wallet.builder()
                    .walletAddress(credentials.getAddress())
                    .walletPrivateKey(credentials.getEcKeyPair().getPrivateKey().toString(16))
                    .build();
            user.updateWallet(wallet);

        } catch (Exception e) {
            log.error("Failed to create wallet for userID {}: {}",user.getId(),e.getMessage(),e);
            throw new CustomException(ExceptionType.FAILED_TO_CREATE_WALLET);
        }

        //초기 토큰지급
        try {
            nhdcService.mint(user.getWallet().getWalletAddress(), BigInteger.valueOf(100000000), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()), serverPrivateKey);
        } catch (Exception e) {
            log.error("초기자금 지급 실패 userID: {} 오류메시지 : {}", user.getId(), e.getMessage(), e);
            throw new CustomException(ExceptionType.FAILTE_TO_FIRESTTOKEN);
        }

        //초기 드림토큰지급
        try {
            drdcService.mint(user.getWallet().getWalletAddress(), BigInteger.valueOf(1000000), CalculateDate.changeLocalDatetoBigInteger(LocalDate.now()), serverPrivateKey);
        } catch (Exception e) {
            log.error("초기드림토큰자금 지급 실패 userID: {} 오류메시지 : {}", user.getId(), e.getMessage(), e);
            throw new CustomException(ExceptionType.FAILTE_TO_FIRESTTOKEN);
        }


        FrDepositAccount newFrDepositAccount = FrDepositAccount.builder()
                .user(user)
                .contractAddress(user.getWallet().getWalletAddress())
                .accountNum(createAccountNum.getUsableAccountNum(4))
                .balance(BigDecimal.valueOf(100000000))
                .build();

        frDepositAccountRepository.save(newFrDepositAccount);
    }

    //가입한 예적금 상품 조회
    @Override
    public List<GetMyAccountResDto> getMyAccounts(int userId) {

        //가입한 적금 조회
        List<GetMyAccountResDto> savingList = savingAccountRepository.findSavingAccountByUserId(userId);
        //가입한 정기예금 조회
        List<GetMyAccountResDto> reDepositList = reDepositAccountRepository.findSavingAccountByUserId(userId);

        // 두 리스트 병합
        List<GetMyAccountResDto> combinedList = new ArrayList<>();
        combinedList.addAll(savingList);
        combinedList.addAll(reDepositList);

        // createdAt 기준으로 오름차순 정렬
        combinedList.sort(Comparator.comparing(GetMyAccountResDto::getCreatedAt));

        return combinedList;

    }

    // 지갑비밀번호 검증
    public void checkWalletPassword(VerifyWalletPasswordReqDto verifyWalletPasswordReqDto, int userId) {
        //유저가 없으면 에러처리
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        if (!bCryptPasswordEncoder.matches(verifyWalletPasswordReqDto.getWalletPassword(), loginUser.getWalletPassword())) {
            throw new CustomException(ExceptionType.WALLETPASSWORD_NOT_MATCH);
        }

    }

    // 지갑비밀번호 수정
    @Transactional
    public void changeWalletPassword(ChangeWalletPasswordReqDto changeWalletPasswordReqDto, int userId) {
        //유저가 없으면 에러처리
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        //비번틀리면 에러처리
        if (!bCryptPasswordEncoder.matches(changeWalletPasswordReqDto.getPassword(), loginUser.getWalletPassword())) {
            throw new CustomException(ExceptionType.WALLETPASSWORD_NOT_MATCH);
        }

        if (!changeWalletPasswordReqDto.getNewPassword1().equals(changeWalletPasswordReqDto.getNewPassword2())) {
            throw new CustomException(ExceptionType.WALLETPASSWORD_CONFIRMATION_MISMATCH);
        }
        loginUser.updateWalletPassword(bCryptPasswordEncoder.encode(changeWalletPasswordReqDto.getNewPassword1()));

    }

    // 로그인비밀번호 변경
    @Transactional
    public void changePassword(ChangePasswordReqDto changePasswordReqDto, int userId) {
        //유저가 없으면 에러처리
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        //비번틀리면 에러처리
        if (!bCryptPasswordEncoder.matches(changePasswordReqDto.getPassword(), loginUser.getPassword())) {
            throw new CustomException(ExceptionType.PASSWORD_NOT_MATCH);
        }

        if (!changePasswordReqDto.getNewPassword1().equals(changePasswordReqDto.getNewPassword2())) {
            throw new CustomException(ExceptionType.PASSWORD_CONFIRMATION_MISMATCH);
        }

        loginUser.updatePassword(bCryptPasswordEncoder.encode(changePasswordReqDto.getNewPassword1()));
    }

    @Override
    public UserInfoResDto getUserInfo(int userId) {
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

         return UserInfoResDto.builder()
                .name(loginUser.getName())
                .birthDay(loginUser.getBirth())
                .email(loginUser.getEmail())
                .loginId(loginUser.getLoginId())
                .type(loginUser.getType().toString())
                .build();
    }

    @Override
    @Transactional
    public void changeTypeToFarmer(int userId, ApplyFarmerReqDto applyFarmerReqDto, MultipartFile buisinessImg) {
        // 회원 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));

        if(user.getType() != UserType.TRAINEE) throw new CustomException(ExceptionType.INVALID_USER_TYPE);

        // 사업자 등록번호 중복 검사
//        if(certifiedRepository.findCertifiedByLicenseNum(applyFarmerReqDto.getLicenseNum()).isPresent()) throw new CustomException(ExceptionType.DUPLICATE_LICENSE_NUM);

        // 사업자 번호 인증
        openApiManager.validateLicense(
                String.valueOf(applyFarmerReqDto.getLicenseNum()),
                applyFarmerReqDto.getRepresentative(),
                applyFarmerReqDto.getRegistrationDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // 이미지 저장
        String imgURl = awsS3Service.uploadFile(buisinessImg);
        // 엔티티 생성
        Certified certified = Certified.builder()
                .representative(applyFarmerReqDto.getRepresentative())
                .openDate(applyFarmerReqDto.getRegistrationDate())
                .licenseNum(applyFarmerReqDto.getLicenseNum())
                .imageUrl(imgURl)
                .user(user)
                .approval(ApprovalStatus.APPROVED)
                .build();

        // 저장
        certifiedRepository.save(certified);

        // 회원 인증 상태 수정
        user.updateType(UserType.FARMER);
    }
}
