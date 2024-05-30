package com.ssafy.nhdream.domain.frdeposit.repository;

import com.ssafy.nhdream.entity.transfer.RecentTransferAddress;
import com.ssafy.nhdream.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecentTransferAddressRepository extends JpaRepository<RecentTransferAddress, Integer> {

    //최대 5개 리스트 가져오기
    @Query("SELECT rta FROM RecentTransferAddress rta WHERE rta.user.id = :userId ORDER BY rta.updatedAt DESC")
    List<RecentTransferAddress> findTop5ByUserIdOrderByUpdatedAtDesc(int userId);

    Optional<RecentTransferAddress> findByUserAndRecipientAddress(User user, String recipientAddress);

}
