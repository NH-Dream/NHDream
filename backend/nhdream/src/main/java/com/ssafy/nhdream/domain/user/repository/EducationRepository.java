package com.ssafy.nhdream.domain.user.repository;

import com.ssafy.nhdream.entity.user.Education;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EducationRepository extends JpaRepository<Education, Integer>, EducationRepositoryCustom {
}
