package com.suprateam.car.dto;

import lombok.Data;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDto {

    private Long id;

    private String text;

    @Lob
    private String description;

    private String sectionID;

    private String answer;

    private String questionID;

    private boolean ocp;

    private String typeQuestion;


    private List<SubQuestionDto> subQuestions = new ArrayList<>();

    private List<ProposalAnswerDto> proposalAnswers = new ArrayList<>();

//    private ProposalAnswerDto chosenAnswer;


//    private List<Occupancy> occupancy;

}
