package com.ssafy.nhdream.common.utils;

import com.ssafy.nhdream.entity.saving.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingAccountNumRepository extends JpaRepository<SavingAccount, Integer> {
}
