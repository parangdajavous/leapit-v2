package com.example.leapit.jobposting.bookmark;

import com.example.leapit.application.ApplicationResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class JobPostingBookmarkResponse {

    // 개인 마이페이지 북마크 관리현황 전체 DTO
    @Data
    public static class ViewDTO {
        private List<ItemDTO> bookmarks;
        private ApplicationResponse.StatusDTO status;

        public ViewDTO(List<JobPostingBookmarkResponse.ItemDTO> bookmarks, ApplicationResponse.StatusDTO status) {
            this.bookmarks = bookmarks;
            this.status = status;
        }
    }

    // 공고 스크랩 목록 DTO
    @Data
    public static class ItemDTO {
        private Integer jobPostingId;
        private String companyName;
        private String jobPostingTitle;
        private LocalDate DeadLine;

        public ItemDTO(Integer jobPostingId, String companyName, String jobPostingTitle, LocalDate deadLine) {
            this.jobPostingId = jobPostingId;
            this.companyName = companyName;
            this.jobPostingTitle = jobPostingTitle;
            DeadLine = deadLine;
        }
    }

}
