package com.suprateam.car.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserDto {
    private Long id;

    private String email;

    private String fullName;

    private String pw;

    private String checkPassword;

    private String role;

    private String companyName;
//    private Company company;

    private Date createDate;

    private Date validUntil;

    private boolean active;
}
