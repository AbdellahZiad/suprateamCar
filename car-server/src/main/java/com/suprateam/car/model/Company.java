package com.suprateam.car.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
public class Company implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String address;

    @Column(unique = true)
    private String companyID;

    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    @JsonIgnore
    private java.util.List<SurveyParameters> surveyParameters;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    private java.util.List<SmeUser> smeUsers;

}
