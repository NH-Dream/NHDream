package com.ssafy.nhdream.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TrainingReviewResDto {

    private int totalPage;

    List<TrainingReviewContentDto> trainingReviewContentDtoList;

}
