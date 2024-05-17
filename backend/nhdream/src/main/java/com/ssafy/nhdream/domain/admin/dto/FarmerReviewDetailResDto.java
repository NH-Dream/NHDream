package com.ssafy.nhdream.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class FarmerReviewDetailResDto {

    private String userName;

    private String email;

    private String phone;

    private LocalDate birthDate;

    private String representative;

    private LocalDate openDate;

    private long licenseNum;

    private String businessLicense;

}
