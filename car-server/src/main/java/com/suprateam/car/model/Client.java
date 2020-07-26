package com.suprateam.car.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Client implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(mappedBy = "client")
    private AddressCl address;

    private Double AnnualRevenue;

    private Double sumInsured;

    private Double employeesNumber;

    private Double numberOfYearsInBusiness;

    private String financialStability;

    //    private Double seniority;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    java.util.List<SurveyUser> surveyUser;
}
