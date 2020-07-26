package com.suprateam.car.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Rates implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Double score;

    private String natCatCode;

    private String typeOfOccupancy;

    private String zipCodeID;

    private String key;

    private Double rate;


    @ManyToOne()
    @JsonIgnore
    Occupancy occupancy;

    @ManyToOne(targetEntity = RiskLocationAndNatCatType.class)
    private RiskLocationAndNatCatType riskLocationAndNatCatType;
}
