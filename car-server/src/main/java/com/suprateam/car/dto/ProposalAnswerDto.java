package com.suprateam.car.dto;

import com.suprateam.car.model.Answer;
import lombok.Data;

@Data
public class ProposalAnswerDto {

    private Long id;

    private Answer answer;

    private Double score;

    private String textAnswer;

    private String sectionID;

    private boolean killed;

    private String questionID;

    private String answerID;

    private String description;

    private String sensitiveFactor;

    private String discountLoading;
}
