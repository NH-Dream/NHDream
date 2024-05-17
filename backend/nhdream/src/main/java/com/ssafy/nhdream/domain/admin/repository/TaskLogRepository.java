package com.ssafy.nhdream.domain.admin.repository;

import com.ssafy.nhdream.entity.admin.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskLogRepository extends JpaRepository<TaskLog, Integer> {
    @Query("SELECT tl FROM TaskLog tl ORDER BY tl.createdAt")
    List<TaskLog> failedTaskLogList();
}
