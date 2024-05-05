package com.ssafy.nhdream.domain.loan.service;

import com.ssafy.nhdream.domain.loan.dto.LoanListReqDto;
import com.ssafy.nhdream.domain.loan.dto.LoanReqDto;

public interface LoanService {

    LoanListReqDto getLoanList();
    LoanReqDto getLoanAccountDetail(int id);

}
