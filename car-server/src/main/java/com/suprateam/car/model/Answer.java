package com.suprateam.car.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data

public class Answer implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String surveyId;

    private String questionId;

    private String sectionID;

    private String sectionName;

    private String answerId;

    private String textQuestion;

    private String textAnswers;

    private String sensitiveFactor;

    private Double discountLoading;

    private Double score;

//
//    @OneToMany
//    List<Media> mediaList;

    @OneToOne()
    @JsonIgnore
    private ProposalAnswer proposalAnswer;

    @ManyToOne(targetEntity = SurveyUser.class)
    @JsonIgnore
    private SurveyUser surveyUser;


}
