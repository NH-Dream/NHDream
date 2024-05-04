package com.ssafy.nhdream.domain.loan.repository;

import com.ssafy.nhdream.entity.loan.LoanOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanOptions, Integer> {

    @Query("SELECT l FROM LoanOptions l")
    List<LoanOptions> findAllByLoanOptions();

}
