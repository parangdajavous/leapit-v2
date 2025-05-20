package com.example.leapit.jobposting;

import com.example.leapit.common.enums.CareerLevel;
import com.example.leapit.common.enums.EducationLevel;
import com.example.leapit.common.region.SubRegion;
import com.example.leapit.companyinfo.CompanyInfo;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

public class JobPostingResponse {

    @Data
    public static class SaveDTO {
        private List<String> positionTypes;
        private List<String> techStackCodes;
        private List<RegionDTO> regions;
        private List<CareerLevel> careerLevels;
        private List<EducationLevel> educationLevels;

        public SaveDTO(List<String> positionTypes, List<String> techStackCodes, List<RegionDTO> regions, List<CareerLevel> careerLevels, List<EducationLevel> educationLevels) {
            this.positionTypes = positionTypes;
            this.techStackCodes = techStackCodes;
            this.regions = regions;
            this.careerLevels = careerLevels;
            this.educationLevels = educationLevels;
        }
    }

    @Data
    public static class RegionDTO {
        private Integer id;
        private String name;
        private List<SubRegionDTO> subRegions;

        public RegionDTO(Integer id, String name, List<SubRegion> subRegions) {
            this.id = id;
            this.name = name;
            this.subRegions = subRegions.stream()
                    .map(sr -> new SubRegionDTO(sr.getId(), sr.getName()))
                    .toList();
        }
    }

    @Data
    public static class SubRegionDTO {
        private Integer id;
        private String name;

        public SubRegionDTO(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Data
    public static class DetailPersonalDTO {
        private DTO companyDTO;
        private CompanyInfoDTO companyInfo;

        public DetailPersonalDTO(DTO companyDTO, CompanyInfoDTO companyInfo) {
            this.companyDTO = companyDTO;
            this.companyInfo = companyInfo;
        }

        @Data
        public static class CompanyInfoDTO {
            private Integer id;
            private String logoImage;
            private String companyName;
            private LocalDate establishmentDate;
            private String mainService;

            public CompanyInfoDTO(CompanyInfo companyInfo) {
                this.id = companyInfo.getId();
                this.logoImage = companyInfo.getLogoImage();
                this.companyName = companyInfo.getCompanyName();
                this.establishmentDate = companyInfo.getEstablishmentDate();
                this.mainService = companyInfo.getMainService();
            }
        }
    }

    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String positionType;
        private String minCareerLevel;
        private String maxCareerLevel;
        private String educationLevel;
        private Integer addressRegionId;
        private Integer addressSubRegionId;
        private String addressDetail;
        private String serviceIntro;
        private LocalDate deadline;
        private String responsibility;
        private String qualification;
        private String preference;
        private String benefit;
        private String additionalInfo;
        private Integer viewCount;
        private String createdAt;

        private List<String> techStacks;

        public DTO(JobPosting jobPosting) {
            this.id = jobPosting.getId();
            this.title = jobPosting.getTitle();
            this.positionType = jobPosting.getPositionType();
            this.minCareerLevel = jobPosting.getMinCareerLevel().label;
            this.maxCareerLevel = jobPosting.getMaxCareerLevel().label;
            this.educationLevel = jobPosting.getEducationLevel().label;
            this.addressRegionId = jobPosting.getAddressRegionId();
            this.addressSubRegionId = jobPosting.getAddressSubRegionId();
            this.addressDetail = jobPosting.getAddressDetail();
            this.serviceIntro = jobPosting.getServiceIntro();
            this.deadline = jobPosting.getDeadline();
            this.responsibility = jobPosting.getResponsibility();
            this.qualification = jobPosting.getQualification();
            this.preference = jobPosting.getPreference();
            this.benefit = jobPosting.getBenefit();
            this.additionalInfo = jobPosting.getAdditionalInfo();
            this.viewCount = jobPosting.getViewCount();
            this.createdAt = jobPosting.getCreatedAt().toString();
            this.techStacks = jobPosting.getJobPostingTechStacks().stream()
                    .map(jpts -> jpts.getTechStack())
                    .toList();
        }
    }

    // 진행중과 마감된 리스트 조회
    @Data
    public static class ListDTO {
        private Integer jobPostingId;
        private String title;

        public ListDTO(Integer jobPostingId, String title) {
            this.jobPostingId = jobPostingId;
            this.title = title;
        }
    }
}