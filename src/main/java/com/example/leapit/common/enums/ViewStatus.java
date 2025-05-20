package com.example.leapit.common.enums;

public enum ViewStatus {
    VIEWED("열람"),
    UNVIEWED("미열람");

    public String label;

    ViewStatus(String label) {
        this.label = label;
    }
}