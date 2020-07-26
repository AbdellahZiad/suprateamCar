package com.suprateam.car.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Section implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String sectionID;

    private String companyID;

    private String surveyID;

    private String iconSection;

    private boolean lastSurvey;

    @OneToMany(mappedBy = "section",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private java.util.List<Question> questionList;


}
