package com.suprateam.car.dto;


import com.suprateam.car.model.Answer;
import lombok.Data;

import java.util.List;

@Data
public class SurveyDetailsDto {

    private String sectionName;
    private List<Answer> answer;

    private String decision;
}
