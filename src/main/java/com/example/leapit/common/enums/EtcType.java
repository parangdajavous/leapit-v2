package com.example.leapit.common.enums;

public enum EtcType {
    CERTIFICATE("자격증"),
    LANGUAGE("어학"),
    AWARD("수상"),
    OTHER("기타");

    public String label;

    EtcType(String label) {
        this.label = label;
    }
}
