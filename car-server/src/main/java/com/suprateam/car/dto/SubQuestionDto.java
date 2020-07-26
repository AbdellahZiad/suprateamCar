package com.suprateam.car.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data

public class SubQuestionDto {

    private Long id;

    private String text;

    private String description;

    private String sectionID;

    private String answer;

    private String questionID;

    private boolean ocp;

    private String typeQuestion;

    private List<ProposalAnswerDto> proposalAnswers = new ArrayList<>();
}
