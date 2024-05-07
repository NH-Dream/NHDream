package com.ssafy.nhdream.domain.loan.service;

import com.ssafy.nhdream.domain.loan.dto.*;

public interface LoanService {

    LoanListResDto getLoanList();
    LoanResDto getLoanAccountDetail(int id);
    int createLoanOption(LoanOptionReqDto loanOptionReqDto);
    int createLoanApproval(LoanApprovalReqDto loanApprovalReqDto);
    void createLoanAccount();
    int createLoanProduct(LoanProductReqDto loanProductReqDto);
    int updateLoanProduct(LoanProductReqDto loanProductReqDto);



}
