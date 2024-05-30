package com.ssafy.nhdream.domain.frdeposit.repository;

import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.frdeposit.FrDepositTransaction;
import com.ssafy.nhdream.entity.transfer.AutomaticTransferTask;
import com.ssafy.nhdream.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AutomaticTransferTaskRepository extends JpaRepository<AutomaticTransferTask, Integer> {
    @Query("SELECT att FROM AutomaticTransferTask att WHERE att.user = :user AND att.isActive = true ORDER BY att.updatedAt DESC")
    Page<AutomaticTransferTask> getAutomaticTransferTaskList(User user, Pageable pageable);

    //자동이체돌릴리스트 자유입출금
    @Query("SELECT att FROM AutomaticTransferTask att WHERE att.nextScheduleTime BETWEEN :yesterday and :today AND att.isActive = true AND att.type = 0")
    List<AutomaticTransferTask> getAutomaticTransferTaskFrDeposit(LocalDate yesterday,LocalDate today);

    //자동이체돌릴리스트 적금
    @Query("SELECT att FROM AutomaticTransferTask att WHERE att.nextScheduleTime BETWEEN :yesterday and :today AND att.isActive = true AND att.type = 1")
    List<AutomaticTransferTask> getAutomaticTransferTaskSaving(LocalDate yesterday,LocalDate today);

    //자동이체돌릴리스트 대출
    //자동이체돌릴리스트 적금
    @Query("SELECT att FROM AutomaticTransferTask att WHERE att.nextScheduleTime BETWEEN :yesterday and :today AND att.isActive = true AND att.type = 2")
    List<AutomaticTransferTask> getAutomaticTransferTaskLoan(LocalDate yesterday,LocalDate today);
}
