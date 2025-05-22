package com.example.leapit.companyinfo;

import com.example.leapit._core.util.Base64Util;
import com.example.leapit.jobposting.JobPosting;
import com.example.leapit.jobposting.techstack.JobPostingTechStack;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CompanyInfoResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String logoImage; // Base64로 인코딩된 로고 이미지 문자열
        private String companyName;
        private LocalDate establishmentDate;
        private String formattedEstablishmentInfo;
        private String address;
        private String mainService;
        private String introduction;
        private String image;  // Base64로 인코딩된 대표 이미지 문자열
        private String benefit;

        public DTO(CompanyInfo companyInfo) {
            this.id = companyInfo.getId();
            this.companyName = companyInfo.getCompanyName();
            this.establishmentDate = companyInfo.getEstablishmentDate();
            this.formattedEstablishmentInfo = formatEstablishmentInfo(companyInfo.getEstablishmentDate());
            this.address = companyInfo.getAddress();
            this.mainService = companyInfo.getMainService();
            this.introduction = companyInfo.getIntroduction();
            this.benefit = companyInfo.getBenefit();

            // 로고 이미지 Base64 변환
            try {
                if (companyInfo.getLogoImage() != null) {
                    byte[] logoBytes = Base64Util.readImageAsByteArray(companyInfo.getLogoImage());
                    this.logoImage = Base64Util.encodeAsString(logoBytes, "image/png");
                }
            } catch (Exception e) {
                this.logoImage = null;
            }

            // 대표 이미지 Base64 변환
            try {
                if (companyInfo.getImage() != null) {
                    byte[] imageBytes = Base64Util.readImageAsByteArray(companyInfo.getImage());
                    this.image = Base64Util.encodeAsString(imageBytes, "image/png");
                }
            } catch (Exception e) {
                this.image = null;
            }
        }

        private String formatEstablishmentInfo(LocalDate date) {
            if (date == null) return "정보 없음";

            LocalDate now = LocalDate.now();
            Period period = Period.between(date, now);
            int years = period.getYears();

            String year = String.valueOf(date.getYear());
            String month = String.valueOf(date.getMonthValue());

            return years + "년차 (" + year + "년 " + month + "월 설립)";
        }
    }

    @Data
    public static class DetailDTO {
        private Integer id;
        private String logoImage; // Base64 인코딩된 로고 이미지
        private String companyName;
        private LocalDate establishmentDate;
        private String formattedEstablishmentInfo;
        private String address;
        private String mainService;
        private String introduction;
        private String image; // Base64 인코딩된 대표 이미지
        private String benefit;
        private Integer jobPostingCount;

        private List<JobPostingDTO> jobPostings = new ArrayList<>(); // 절대 null 아님

        @Data
        public static class JobPostingDTO {
            private Integer id;
            private String title;
            private LocalDate deadline;
            private String dDayLabel;
            private List<TechStackDTO> techStacks = new ArrayList<>();

            @Data
            public static class TechStackDTO {
                private String name;

                public TechStackDTO(String name) {
                    this.name = name != null ? name : "";
                }
            }

            public JobPostingDTO(JobPosting jobPostings, List<JobPostingTechStack> techStacks) {
                this.id = jobPostings.getId();
                this.title = jobPostings.getTitle();
                this.deadline = jobPostings.getDeadline();
                this.dDayLabel = calculateDdayLabel(deadline);
                if (techStacks != null) {
                    this.techStacks = techStacks.stream()
                            .map(stack -> new TechStackDTO(stack.getTechStack()))
                            .toList();
                }
            }

            private String calculateDdayLabel(LocalDate deadline) {
                if (deadline == null) return "마감일 없음";
                int days = (int) ChronoUnit.DAYS.between(LocalDate.now(), deadline);
                return days < 0 ? "마감" : "D-" + days;
            }
        }

        public DetailDTO(CompanyInfo companyInfo, Integer userId, Integer jobPostingCount,
                         List<JobPosting> jobPostings, List<JobPostingTechStack> techStacks) {

            this.id = companyInfo.getId();
            this.companyName = companyInfo.getCompanyName();
            this.establishmentDate = companyInfo.getEstablishmentDate();
            this.formattedEstablishmentInfo = formatEstablishmentInfo(companyInfo.getEstablishmentDate());
            this.address = companyInfo.getAddress();
            this.mainService = companyInfo.getMainService();
            this.introduction = companyInfo.getIntroduction();
            this.benefit = companyInfo.getBenefit();
            this.jobPostingCount = jobPostingCount;

            // 이미지 처리
            this.logoImage = encodeImageSafe(companyInfo.getLogoImage(), "image/png");
            this.image = encodeImageSafe(companyInfo.getImage(), "image/png");

            // 공고 리스트 생성
            if (jobPostings != null) {
                for (JobPosting jobPosting : jobPostings) {
                    List<JobPostingTechStack> matchedStacks = techStacks != null
                            ? techStacks.stream()
                            .filter(stack -> stack.getJobPosting() != null
                                    && stack.getJobPosting().getId().equals(jobPosting.getId()))
                            .toList()
                            : new ArrayList<>();

                    this.jobPostings.add(new JobPostingDTO(jobPosting, matchedStacks));
                }
            }
        }

        private String encodeImageSafe(String filename, String mimeType) {
            try {
                if (filename != null && !filename.isBlank()) {
                    byte[] imageBytes = Base64Util.readImageAsByteArray(filename);
                    return Base64Util.encodeAsString(imageBytes, mimeType);
                }
            } catch (Exception e) {
                // 로그만 찍고 무시
                System.out.println("이미지 인코딩 실패: " + filename);
            }
            return null;
        }

        private String formatEstablishmentInfo(LocalDate date) {
            if (date == null) return "정보 없음";
            int years = Period.between(date, LocalDate.now()).getYears();
            return years + "년차 (" + date.getYear() + "년 " + date.getMonthValue() + "월 설립)";
        }
    }

    @Data
    public static class TechStackDTO {
        private String name;

        public TechStackDTO(String name) {
            this.name = name;
        }
    }
}