package com.suprateam.car.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;

@Data
@Entity
public class AddressCl {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private java.util.List<Country> country = new ArrayList<>();

    private String city;

    private String rue;

    private String postalCode;

    private String number;

    @OneToOne()
    private Client client;
}
