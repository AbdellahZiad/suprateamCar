package com.suprateam.car.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
public class SmeUser implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 254,unique = true)
    private String email;


    @Column(name = "name", length = 50)
    private String fullName;

    @Lob
    @Column(name = "pw")
    private String pw;

//    @Column(name = "role", length = 50)
//    private String role;

    @ManyToOne(targetEntity = Company.class)
    private Company company;

    private Date createDate;

    private Date validUntil;

    private boolean active = true;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private java.util.List<SurveyUser> surveyUserList;


    @ManyToOne(targetEntity = Role.class)
    private Role role;

}
