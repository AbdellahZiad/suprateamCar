package com.suprateam.car.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class RiskLocationAndNatCatType implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zip_code_id")
    private String zipCodeID;

    @Column(name = "zip_code")
    private String zipCode;

    private String natCatWS;

    private String natCatFLOOD;

    private String natCatEQ;

    @OneToMany(mappedBy = "riskLocationAndNatCatType")
    private java.util.List<Rates> rates;

}
