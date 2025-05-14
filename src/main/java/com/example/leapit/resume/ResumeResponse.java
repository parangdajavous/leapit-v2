package com.example.leapit.resume;

import lombok.Data;
import java.util.List;

public class ResumeResponse {
    @Data
    public static class DTO {
        private Integer id;
        private Integer userId;
        private String title;
        private String photoUrl;
        private String summary;
        private String positionType;
        private String selfIntroduction;
        private String createdAt;
        private String updatedAt;

        public DTO(Resume resume) {
            this.id = resume.getId();
            this.userId = resume.getUser().getId();
            this.title = resume.getTitle();
            this.photoUrl = resume.getPhotoUrl();
            this.summary = resume.getSummary();
            this.positionType = resume.getPositionType();
            this.selfIntroduction = resume.getSelfIntroduction();
            this.createdAt = resume.getCreatedAt().toString();
            this.updatedAt = resume.getUpdatedAt().toString();
        }
    }

    @Data
    public static class ListDTO{
        private List<DTO> resumes;

        public ListDTO(List<Resume> resumes) {
            this.resumes = resumes.stream().map(resume -> new DTO(resume)).toList();
        }
    }
}
