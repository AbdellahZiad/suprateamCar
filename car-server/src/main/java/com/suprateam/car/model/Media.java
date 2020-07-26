package com.suprateam.car.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Media implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    private String name;

    private String mediaType;

    private String sectionTag;

    private String extension;

    @ManyToOne(targetEntity = SurveyUser.class)
    SurveyUser surveyUser;
}
