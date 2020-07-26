package com.suprateam.car.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Question implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Lob
    @Column(name="description", length=2000000000)
    private String description;

    private String sectionID;

    private String questionID;

    private String answer;

    private boolean ocp;

    public Question(String text) {
        this.text = text;
    }

    @ManyToOne(targetEntity = Section.class,fetch = FetchType.LAZY)
    private Section section;

    @ManyToOne(targetEntity = TypeQuestion.class)
    private TypeQuestion typeQuestion;

    @ManyToOne(targetEntity = SurveyParameters.class)
    private SurveyParameters surveyParameters;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private java.util.List<ProposalAnswer> proposalAnswers;


    @OneToMany(mappedBy ="question",cascade = CascadeType.ALL)
    private java.util.List<Occupancy> occupancy;
}
