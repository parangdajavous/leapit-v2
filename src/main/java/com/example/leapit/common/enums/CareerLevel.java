package com.example.leapit.common.enums;

public enum CareerLevel {
    YEAR_0("신입", 0),
    YEAR_1("1년차", 1),
    YEAR_2("2년차", 2),
    YEAR_3("3년차", 3),
    YEAR_4("4년차", 4),
    YEAR_5("5년차", 5),
    YEAR_6("6년차", 6),
    YEAR_7("7년차", 7),
    YEAR_8("8년차", 8),
    YEAR_9("9년차", 9),
    OVER_10("10년차 이상", 10);

    public String label;
    public Integer value;

    CareerLevel(String label, Integer value) {
        this.label = label;
        this.value = value;
    }
}