package com.suprateam.car.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnswersDto {

    private String surveyID;
    private String companyID;
    private java.util.List<SectionDto> sectionDtoList = new ArrayList<>();
    private List<MediaTypeDto> mediaTypeDtoList = new ArrayList<>();


}
