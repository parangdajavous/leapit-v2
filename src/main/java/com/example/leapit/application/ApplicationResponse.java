package com.example.leapit.application;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class ApplicationResponse {

    // 지원 현황 목록 + 통계
    @Data
    public static class MyPageDTO {
        private StatusDTO status;
        private List<ItemDTO> itemDTOs;

        public MyPageDTO(StatusDTO status, List<ItemDTO> itemDTOs) {
            this.status = status;
            this.itemDTOs = itemDTOs;
        }
        // 지원 형황 목록
        @Data
        public static class ItemDTO {

            private String companyName;
            private String jobTitle;
            private LocalDate appliedDate;
            private Integer resumeId;
            private Integer jobPostingId;
            private String result;

            public ItemDTO(String companyName, String jobTitle, LocalDate appliedDate,
                           Integer resumeId, Integer jobPostingId, String result) {
                this.companyName = companyName;
                this.jobTitle = jobTitle;
                this.appliedDate = appliedDate;
                this.resumeId = resumeId;
                this.jobPostingId = jobPostingId;
                this.result = result;
            }
        }
    }

    // 지원 현황 통계
    @Data
    public static class StatusDTO {
        private Long total;
        private Long passed;
        private Long failed;

        public StatusDTO(Long total, Long passed, Long failed) {
            this.total = total;
            this.passed = passed;
            this.failed = failed;
        }
    }


}
