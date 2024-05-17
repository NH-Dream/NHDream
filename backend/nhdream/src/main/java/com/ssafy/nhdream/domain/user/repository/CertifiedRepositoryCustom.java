package com.ssafy.nhdream.domain.user.repository;

import com.ssafy.nhdream.domain.admin.dto.FarmerReviewContentDto;
import com.ssafy.nhdream.domain.admin.dto.TrainingReviewContentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CertifiedRepositoryCustom {
    Page<FarmerReviewContentDto> getFarmerReviewList(String username, Pageable pageable);
}
