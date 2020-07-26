package com.suprateam.car.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class SurveyParameters implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double thresholdMax;

    private Double thresholdMin;

    private Long reload;

    private Double score;

    private String templateName;

    private String loadedBy;

    private Date loadedOn;

    private String status;

    private String statusUploadTemplate;


    //    @Column(unique = true)
    private String surveyID;

    private String companyID;

    private boolean active = true;

    @ToString.Exclude
    @OneToMany(mappedBy = "surveyParameters", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private java.util.List<Question> questionList;

    @ManyToOne(targetEntity = Company.class)
    @JsonIgnore
    private Company company;

    @OneToMany(mappedBy = "surveyParameters", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private java.util.List<SurveyUser> surveyUserList;


}
