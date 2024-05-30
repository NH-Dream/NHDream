package com.ssafy.nhdream.domain.loan.service;

import com.ssafy.nhdream.domain.loan.dto.*;
import com.ssafy.nhdream.domain.user.dto.TransactionListResDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {

    LoanListResDto getLoanList();
    LoanResDto getLoanAccountDetail(int id);
    int createLoanOption(LoanOptionReqDto loanOptionReqDto);
    LoanOptionDetailResDto getLoanOptionDetail(int id);
    int createLoanApproval(int userId, int loanOptionId, List<MultipartFile> files);
    int createLoanAccount(int id);
    int createLoanProduct(LoanProductReqDto loanProductReqDto);
    int updateLoanProduct(LoanProductReqDto loanProductReqDto);
    int updateLoanApproval(int id);
    int deleteLoanProduct(int id, LocalDate deletedAt);
    LoanProductResDto getLoanProductDetail(int id);
    MyLoanListResDto getMyLoanList(int id);
    MyLoanDetailResDto getMyLoanDetail(int id);

    // 모든 거래 기록 조회
    TransactionListResDto getTransactionList(int id);

}
