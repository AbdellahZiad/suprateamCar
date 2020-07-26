package com.suprateam.car.constants;


public enum YN {

    YES("Yes"),
    NO("No");

    private String value;

    YN(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
