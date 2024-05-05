package com.ssafy.nhdream.domain.saving.repository;

import com.ssafy.nhdream.entity.saving.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingAccountRepository extends JpaRepository<SavingAccount, Integer> {
}
