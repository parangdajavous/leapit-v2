package com.example.leapit.application;
import com.example.leapit.common.enums.BookmarkStatus;
import com.example.leapit.common.enums.PassStatus;
import com.example.leapit.common.enums.ViewStatus;
import com.example.leapit.jobposting.JobPostingResponse;
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

    // 기업 지원자현황 관리 페이지 RespDTO
    @Data
    public static class ApplicantPageDTO {
        private List<ApplicantListDTO> applicants; // 지원받은 이력서 목록 조회
        private List<JobPostingResponse.ListDTO> allPositions; // 전체 공고 조회
        private List<JobPostingResponse.ListDTO> openPositions; // 진행중인 공고 조회
        private List<JobPostingResponse.ListDTO> closedPostions; // 마감된 공고 조회

        public ApplicantPageDTO(List<ApplicantListDTO> applicants, List<JobPostingResponse.ListDTO> allPositions, List<JobPostingResponse.ListDTO> openPositions, List<JobPostingResponse.ListDTO> closedPostions) {
            this.applicants = applicants;
            this.allPositions = allPositions;
            this.openPositions = openPositions;
            this.closedPostions = closedPostions;
        }
    }


    // 지원자 목록 조회
    @Data
    public static class ApplicantListDTO {
        private Integer applicationId;
        private Integer resumeId;
        private String applicantName;
        private String jobTitle;
        private LocalDate appliedDate;
        private BookmarkStatus bookmarkStatus;
        private ViewStatus viewStatus;

        public ApplicantListDTO(Integer applicationId,
                                Integer resumeId,
                                String applicantName,
                                String jobTitle,
                                LocalDate appliedDate,
                                BookmarkStatus bookmarkStatus,
                                ViewStatus viewStatus) {
            this.applicationId = applicationId;
            this.resumeId = resumeId;
            this.applicantName = applicantName;
            this.jobTitle = jobTitle;
            this.appliedDate = appliedDate;
            this.bookmarkStatus = bookmarkStatus;
            this.viewStatus = viewStatus;
        }
    }

    @Data
    public static class UpdatePassDTO {
        private Integer applicationId;
        private PassStatus passStatus;

        public UpdatePassDTO(Integer applicationId, PassStatus passStatus) {
            this.applicationId = applicationId;
            this.passStatus = passStatus;
        }
    }
}

