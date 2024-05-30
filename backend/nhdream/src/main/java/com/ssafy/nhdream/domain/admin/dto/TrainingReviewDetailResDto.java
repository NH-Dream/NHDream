package com.ssafy.nhdream.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class TrainingReviewDetailResDto {

    private int userId;

    private String userName;

    private String email;

    private String phone;

    private LocalDate birthDate;

    private String trainingName;

    private String trainingInstitution;

    private int ordinal;

    private String certificate;


}
