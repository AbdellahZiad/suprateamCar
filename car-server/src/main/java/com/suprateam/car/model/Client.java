package com.suprateam.car.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Client implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String cin;

    private String tel;

    private Date dateDebut;

    private Date dateFin;

    private Integer numberDay;

    private Double total;

    private Double coutEntretienAndFix;

    @OneToMany()
    List<EntretienAndFix> entretienAndFixes;

    @OneToMany()
    List<Voiture> vehicleList;
}
