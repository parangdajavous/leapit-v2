package com.example.leapit.common.enums;

public enum PassStatus {
    PASS("합격"),
    FAIL("불합격"),
    WAITING("미정");

    public String label;

    PassStatus(String label) {
        this.label = label;
    }
}