package com.suprateam.car.constants;

import lombok.Data;

@Data
public class NATCATInputs {

    private String zipCode;

    private String code;

    private String typeNATCAT;

    private String typeOfOccupancy;

    public NATCATInputs(String zipCode, String code,String typeNATCAT) {
        this.code = code;
        this.zipCode =zipCode;
        this.typeNATCAT =typeNATCAT;

    }
}
