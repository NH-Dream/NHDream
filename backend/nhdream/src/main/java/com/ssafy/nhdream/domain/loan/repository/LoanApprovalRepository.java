package com.ssafy.nhdream.domain.loan.repository;

import com.ssafy.nhdream.domain.admin.dto.LoanReviewContentDto;
import com.ssafy.nhdream.entity.loan.LoanApproval;
import com.ssafy.nhdream.entity.user.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface LoanApprovalRepository extends JpaRepository<LoanApproval, Integer>, LoanApprovalRepositoryCustom {

//    @Transactional
//    @Modifying
//    @Query("UPDATE LoanApproval l SET l.type = 1 WHERE l.id = :id")
//    void updateLoanApprovalById(int id);

    @Query("SELECT l FROM LoanApproval l WHERE l.user.id = :id")
    List<LoanApproval> findAllByUserId(int id);

}
