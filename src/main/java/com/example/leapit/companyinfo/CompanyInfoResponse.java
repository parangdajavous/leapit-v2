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
        private String logoImage; // Base64로 인코딩된 로고 이미지 문자열
        private String companyName;
        private LocalDate establishmentDate; // 원본 날짜
        private String formattedEstablishmentInfo; // 포맷된 날짜
        private String address;
        private String mainService;
        private String introduction;
        private String image; // Base64로 인코딩된 대표 이미지 문자열
        private String benefit;
        private Integer jobPostingCount; // 해당 기업의 공고 수

        private List<JobPostingDTO> jobPostings; // 해당 기업의 공고들


        @Data
        public static class JobPostingDTO {
            private Integer id;
            private String title;
            private LocalDate deadline;  // 원본 마감일
            private String dDayLabel; // 포맷된 마감일
            private List<TechStackDTO> techStacks;  // 해당 공고의 기술스택들

            @Data
            public static class TechStackDTO {
                private String name;

                public TechStackDTO(String name) {
                    this.name = name;
                }
            }

            public JobPostingDTO(JobPosting jobPostings, List<JobPostingTechStack> techStacks) {
                this.id = jobPostings.getId();
                this.title = jobPostings.getTitle();
                this.deadline = jobPostings.getDeadline();
                this.dDayLabel = calculateDdayLabel(deadline);
                this.techStacks = techStacks.stream()
                        .map(stack -> new TechStackDTO(stack.getTechStack()))
                        .toList();
            }

            // deadline format 함수
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

            // 공고 목록 생성
            List<JobPostingDTO> jobPostingsDTO = new ArrayList<>();
            for (JobPosting jobPosting : jobPostings) {
                List<JobPostingTechStack> matchedStacks = techStacks.stream()
                        .filter(stack -> stack.getJobPosting() != null &&
                                stack.getJobPosting().getId().equals(jobPosting.getId()))
                        .toList();

                jobPostingsDTO.add(new JobPostingDTO(jobPosting, matchedStacks));
            }
            this.jobPostings = jobPostingsDTO;
        }

        // establishmentDate format 함수
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
    public static class TechStackDTO {
        private String name;

        public TechStackDTO(String name) {
            this.name = name;
        }
    }
}