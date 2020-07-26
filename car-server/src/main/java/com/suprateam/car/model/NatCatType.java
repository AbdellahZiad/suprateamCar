package com.suprateam.car.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class NatCatType implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String natCatWS;

    private String natCatFLOOD;

    private String natCatEQ;

//    @OneToMany(mappedBy = "natCatType")
//    private List<Rates> rates;
}
