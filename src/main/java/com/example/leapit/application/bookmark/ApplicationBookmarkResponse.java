package com.example.leapit.application.bookmark;

import lombok.Data;

public class ApplicationBookmarkResponse {

    @Data
    public static class SaveDTO {
        private Integer bookmarkId;
        private Integer userId;
        private Integer applicationId;
        private String createdAt;

        public SaveDTO(ApplicationBookmark bookmark) {
            this.bookmarkId = bookmark.getId();
            this.userId = bookmark.getUser().getId();
            this.applicationId = bookmark.getApplication().getId();
            this.createdAt = bookmark.getCreatedAt().toString();
        }
    }
}
