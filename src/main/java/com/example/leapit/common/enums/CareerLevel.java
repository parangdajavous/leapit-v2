package com.example.leapit.common.enums;

public enum CareerLevel {
    YEAR_0("신입"),
    YEAR_1("1년차"),
    YEAR_2("2년차"),
    YEAR_3("3년차"),
    YEAR_4("4년차"),
    YEAR_5("5년차"),
    YEAR_6("6년차"),
    YEAR_7("7년차"),
    YEAR_8("8년차"),
    YEAR_9("9년차"),
    OVER_10("10년차 이상");

    private final String label;

    CareerLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}