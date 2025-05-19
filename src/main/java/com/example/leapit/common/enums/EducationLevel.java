package com.example.leapit.common.enums;

public enum EducationLevel {
    NO_PREFERENCE("무관"),
    HIGH_SCHOOL("고등학교"),
    ASSOCIATE("전문학사"),
    BACHELOR("학사"),
    MASTER("석사"),
    DOCTOR("박사");

    public String label;

    EducationLevel(String label) {
        this.label = label;
    }
}