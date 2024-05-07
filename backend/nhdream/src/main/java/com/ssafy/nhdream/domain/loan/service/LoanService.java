package com.ssafy.nhdream.domain.loan.service;

import com.ssafy.nhdream.domain.loan.dto.LoanListResDto;
import com.ssafy.nhdream.domain.loan.dto.LoanResDto;

public interface LoanService {

    LoanListResDto getLoanList();
    LoanResDto getLoanAccountDetail(int id);
    void createLoanOption();
    void createLoanAccount();
    void createLoanApproval();


}
