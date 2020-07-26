package com.suprateam.car.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SectionDto {

    private Long id;
    private String name;
    private String surveyID;
    private String sectionID;
    private String iconSection;
    private java.util.List<QuestionDto> questionList = new ArrayList<>();


}
