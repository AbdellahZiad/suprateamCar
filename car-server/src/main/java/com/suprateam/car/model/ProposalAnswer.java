package com.suprateam.car.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ProposalAnswer implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "proposalAnswer")
    private Answer answer;

    private Double score;

    private String companyID;

    private String surveyID;

    private String textAnswer;

    private String sectionID;

    private boolean killed;

    private String questionID;

    private String answerID;

    private String sensitiveFactor;

//    @Lob
    private String description;

    private Double discountLoading;

    @ManyToOne(targetEntity = Question.class)
    private Question question;


}
