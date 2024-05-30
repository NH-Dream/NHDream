package com.ssafy.nhdream.domain.user.controller;

import com.ssafy.nhdream.common.auth.CustomUserDetails;
import com.ssafy.nhdream.common.response.BaseResponseBody;
import com.ssafy.nhdream.domain.user.dto.*;
import com.ssafy.nhdream.domain.user.service.MailService;
import com.ssafy.nhdream.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name="1.USER", description="USER API")
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @Operation(summary = "회원가입", description = "/users\n\n 사용자의 정보를 통해 회원가입 한다.")
    @PostMapping("")
    @ApiResponse(responseCode = "201", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> join(@RequestBody @Valid JoinReqDto joinReqDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, userService.join(joinReqDto)));
    }

    @Operation(summary = "아이디 중복검사", description = "사용자가 작성한 아이디의 중복검사를 한다.")
    @GetMapping("/duplicateId")
    @ApiResponse(responseCode = "200", description = "성공 \n\n ")
    public ResponseEntity<? extends BaseResponseBody> checkIdDuplicate(@RequestParam
                                                                       @Parameter(description = "중복확인 하고자하는 아이디")
                                                                       String id) {

        userService.checkIdDuplicate(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "회원가입을 위한 이메일 인증", description = "/users/join/auth\n\n 사용자는 회원가입을 위해 이메일 인증을 한다.")
    @PostMapping("/email")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> verifyJoinEmail(@RequestBody @Valid  VerifyEmailReqDto verifyEmailReqDto) throws Exception {

        // 메일 전송 후 코드 받기
        mailService.sendJoinMessage(verifyEmailReqDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "이메일 인증 확인", description = "/users/auth/check\n\n 사용자는 이메일 인증 확인을 한다.")
    @PostMapping("/auth/check")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    public ResponseEntity<? extends BaseResponseBody> verifyEmailCode(@Valid @RequestBody VerifyCodeReqDto verifyCodeReqDto) {

        mailService.verifyAuthCode(verifyCodeReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "교육 인증 신청", description = "/users/apply/education\n\n 사용자는 교육인증신청을 할 수 있다.")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    @PostMapping(value = "/apply/education", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<? extends BaseResponseBody> applyEducation(@RequestPart @Valid ApplyEducationReqDto applyEducationReqDto,
                                                                     @RequestPart(value = "educationImg") MultipartFile educationImg) {

        userService.applyEducation(applyEducationReqDto, educationImg);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "귀농 인증 신청", description = "/users/apply/education\n\n 사용자는 귀농 인증 신청을 할 수 있다.")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    @PostMapping(value = "/apply/farmer", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<? extends BaseResponseBody> applyFarmer(@RequestPart @Valid ApplyFarmerReqDto applyFarmerReqDto,
                                                                  @RequestPart(value = "buisinessImg") MultipartFile buisinessImg) {

       userService.applyFarmer(applyFarmerReqDto, buisinessImg);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(0, "Success"));
    }

    @Operation(summary = "가입한 예적금 조회", description = "/users/account\n\n 사용자는 가입한 예적금 상품을 볼 수 있다.")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    @GetMapping("/account")
    public ResponseEntity<? extends BaseResponseBody> getMyAccounts(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<GetMyAccountResDto> getMyAccountResDtoList = userService.getMyAccounts(userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, getMyAccountResDtoList));
    }

    @Operation(summary = "지갑비밀번호 검사", description = "/users/walletpassword \n\n 사용자는 지갑비밀번호를 검증할 수 있다.")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    @PostMapping("/walletpassword")
    public ResponseEntity<? extends BaseResponseBody> checkWalletPassword(@RequestBody VerifyWalletPasswordReqDto verifyWalletPasswordReqDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        userService.checkWalletPassword(verifyWalletPasswordReqDto, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "지갑비밀번호가 일치합니다."));
    }

    @Operation(summary = "지갑비밀번호 변경", description = "/users/walletpassword/update \n\n 사용자는 지갑비밀번호를 변경할 수 있다.")
    @ApiResponse(responseCode ="200", description = "성공 \n\n Success 반환")
    @PatchMapping("/walletpassword/update")
    public ResponseEntity<? extends BaseResponseBody> updateWalletPassword(@RequestBody ChangeWalletPasswordReqDto changeWalletPasswordReqDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        userService.changeWalletPassword(changeWalletPasswordReqDto, userDetails.getId());
        log.info("userId: {}, 지갑비밀번호 변경", userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "지갑비밀번호가 변경되었습니다."));
    }

    @Operation(summary = "비밀번호 변경", description = "/users/password/update \n\n 사용자는 비밀번호를 변경할 수 있다.")
    @ApiResponse(responseCode ="200", description = "성공 \n\n Success 반환")
    @PatchMapping("/password/update")
    public ResponseEntity<? extends BaseResponseBody> updatePassword(@RequestBody ChangePasswordReqDto changePasswordReqDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        userService.changePassword(changePasswordReqDto, userDetails.getId());
        log.info("userId: {}, 비밀번호 변경", userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "비밀번호가 변경되었습니다."));
    }

    @Operation(summary = "개인 정보 조회", description = "/users\n\n 사용자는 자신의 개인정보를 확인할 수 있다.")
    @ApiResponse(responseCode = "200", description = "성공 \n\n 개인 정보 반환")
    @GetMapping(value = "")
    public ResponseEntity<? extends BaseResponseBody> getUserINfo(Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, userService.getUserInfo(userDetails.getId())));
    }

    @Operation(summary = "농부 타입 변경", description = "/users\n\n 수료자는 농부로 타입 변경 신청을 할 수 있다.")
    @ApiResponse(responseCode = "200", description = "성공 \n\n Success 반환")
    @PatchMapping(value = "/me/type", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<? extends BaseResponseBody> changeTypeToFarmer(Authentication authentication,
                                                                         @RequestPart @Valid ApplyFarmerReqDto applyFarmerReqDto,
                                                                         @RequestPart(value = "buisinessImg") MultipartFile buisinessImg) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        userService.changeTypeToFarmer(userDetails.getId(), applyFarmerReqDto, buisinessImg);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(0, "Success"));
    }

}
