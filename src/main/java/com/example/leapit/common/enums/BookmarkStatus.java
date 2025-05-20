package com.example.leapit.common.enums;

public enum BookmarkStatus {
    BOOKMARKED("스크랩됨"),
    NOT_BOOKMARKED("스크랩안됨");

    public String label;

    BookmarkStatus(String label) {
        this.label = label;
    }
}
