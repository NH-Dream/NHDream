package com.ssafy.nhdream.domain.loan.repository;

import com.ssafy.nhdream.domain.admin.dto.LoanReviewContentDto;
import com.ssafy.nhdream.domain.admin.dto.LoanReviewResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanApprovalRepositoryCustom {

    Page<LoanReviewContentDto> getLoanReviewList(String username, String reviewStatus, Pageable pageable);
}
