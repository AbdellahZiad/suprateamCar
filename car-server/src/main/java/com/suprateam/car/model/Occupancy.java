package com.suprateam.car.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class Occupancy implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usTitle;

    private String sectionID;

    private String questionID;

    private String answerID;

    private String riskAssessment;

    private Double usCode;

    private String typeOfOccupancy;

    private String additionalQuestion;

    private Double seqNo;

    private Double capacityLimit;

    private Double baseRate;


    @ManyToOne(targetEntity = Question.class)
    @JsonIgnore
    Question question;

    @OneToMany(mappedBy = "occupancy",cascade = CascadeType.ALL)
    private java.util.List<Rates> ratesList;

}
