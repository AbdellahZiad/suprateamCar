package com.suprateam.car.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TypeQuestion implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @OneToMany(mappedBy = "typeQuestion")
    java.util.List<Question> questionList;
}
