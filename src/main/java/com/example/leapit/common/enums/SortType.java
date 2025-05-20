package com.example.leapit.common.enums;

public enum SortType {
    POPULAR("인기순"),
    LATEST("최신순");

    public String label;

    SortType(String label) {
        this.label = label;
    }
}
