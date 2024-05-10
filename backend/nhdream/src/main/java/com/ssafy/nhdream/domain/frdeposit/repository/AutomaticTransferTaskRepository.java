package com.ssafy.nhdream.domain.frdeposit.repository;

import com.ssafy.nhdream.entity.transfer.AutomaticTransferTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomaticTransferTaskRepository extends JpaRepository<AutomaticTransferTask, Integer> {
}
