package com.example.leapit.application;

import com.example.leapit.common.enums.BookmarkStatus;
import com.example.leapit.common.enums.PassStatus;
import com.example.leapit.common.enums.ViewStatus;
import lombok.Data;

public class ApplicationRequest {

    // 기업 지원자 현황 관리
    @Data
    public static class ApplicantListDTO {
        private Integer jobPostingId;
        private Boolean isClosed;

        private ViewStatus viewStatus;           // 열람 여부
        private BookmarkStatus bookmarkStatus;   // 스크랩 여부
        private PassStatus passStatus;           // 합격 여부
    }

    @Data
    public static class UpdatePassDTO {
        private PassStatus passStatus;
    }
}