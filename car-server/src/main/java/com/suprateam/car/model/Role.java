package com.suprateam.car.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column
    private String role;

    @Column
    private String description;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<SmeUser> smeUser;

    public Role(String name, String description) {
        this.role = name;
        this.description = description;
    }

    public Role() {
    }

}
