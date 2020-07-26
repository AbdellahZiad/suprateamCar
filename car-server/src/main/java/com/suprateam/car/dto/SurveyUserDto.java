package com.suprateam.car.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SurveyUserDto {
    private Long id;

    private Double thresholdMax;

    private Double thresholdMin;

    private Double score;

    private String templateName;

    private String loadedBy;

    private Date loadedOn;

    private String status;

    //    @Column(unique = true)
    private String surveyID;

    private String companyID;

    private String companyName;

}
