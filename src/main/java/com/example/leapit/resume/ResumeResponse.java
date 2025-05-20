package com.example.leapit.resume;

import com.example.leapit._core.util.Base64Util;
import com.example.leapit.common.enums.CareerLevel;
import com.example.leapit.common.enums.EducationLevel;
import com.example.leapit.common.enums.EtcType;
import com.example.leapit.resume.education.Education;
import com.example.leapit.resume.etc.Etc;
import com.example.leapit.resume.experience.Experience;
import com.example.leapit.resume.link.Link;
import com.example.leapit.resume.project.Project;
import com.example.leapit.resume.training.Training;
import com.example.leapit.user.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class ResumeResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String photoUrl;

        private String summary;
        private String positionType;
        private String selfIntroduction;

        private String createdAt;
        private String updatedAt;

        private List<String> resumeTechStacks;
        private List<EducationDTO> educations;
        private List<ProjectDTO> projects;
        private List<ExperienceDTO> experiences;
        private List<LinkDTO> links;
        private List<TrainingDTO> trainings;
        private List<EtcDTO> etcs;

        @Data
        public static class EducationDTO{
            private Integer id;
            private LocalDate graduationDate;
            private Boolean isDropout;
            private EducationLevel educationLevel;
            private String schoolName;
            private String major;
            private String gpa;
            private String gpaScale;

            public EducationDTO(Education education) {
                this.id = education.getId();
                this.graduationDate = education.getGraduationDate();
                this.isDropout = education.getIsDropout();
                this.educationLevel = education.getEducationLevel();
                this.schoolName = education.getSchoolName();
                this.major = education.getMajor();
                this.gpa = education.getGpa() != null ? education.getGpa().toString() : null;
                this.gpaScale = education.getGpaScale() != null ? education.getGpaScale().toString() : null;
            }
        }

        @Data
        public static class ProjectDTO{
            private Integer id;
            private LocalDate startDate;
            private LocalDate endDate;
            private Boolean isOngoing;
            private String title;
            private String summary;
            private String description;
            private String repositoryUrl;
            private List<String> techStacks;

            public ProjectDTO(Project project) {
                this.id = project.getId();
                this.startDate = project.getStartDate();
                this.endDate = project.getEndDate();
                this.isOngoing = project.getIsOngoing();
                this.title = project.getTitle();
                this.summary = project.getSummary();
                this.description = project.getDescription();
                this.repositoryUrl = project.getRepositoryUrl();
                this.techStacks = project.getProjectTechStacks()
                        .stream()
                        .map(pts -> pts.getTechStack())
                        .toList();
            }
        }

        @Data
        public static class ExperienceDTO{
            private Integer id;
            private LocalDate startDate;
            private LocalDate endDate;
            private Boolean isEmployed;
            private String companyName;
            private String summary;
            private String position;
            private String responsibility;
            private List<String> techStacks;

            public ExperienceDTO(Experience experience) {
                this.id = experience.getId();
                this.startDate = experience.getStartDate();
                this.endDate = experience.getEndDate();
                this.isEmployed = experience.getIsEmployed();
                this.companyName = experience.getCompanyName();
                this.summary = experience.getSummary();
                this.position = experience.getPosition();
                this.responsibility = experience.getResponsibility();
                this.techStacks = experience.getExperienceTechStacks()
                        .stream()
                        .map(ets -> ets.getTechStack())
                        .toList();
            }
        }

        @Data
        public static class LinkDTO{
            private Integer id;
            private String title;
            private String url;

            public LinkDTO(Link link) {
                this.id = link.getId();
                this.title = link.getTitle();
                this.url = link.getUrl();
            }
        }

        @Data
        public static class TrainingDTO{
            private Integer id;
            private LocalDate startDate;
            private LocalDate endDate;
            private Boolean isOngoing;
            private String courseName;
            private String institutionName;
            private String description;
            private List<String> techStacks;

            public TrainingDTO(Training training) {
                this.id = training.getId();
                this.startDate = training.getStartDate();
                this.endDate = training.getEndDate();
                this.isOngoing = training.getIsOngoing();
                this.courseName = training.getCourseName();
                this.institutionName = training.getInstitutionName();
                this.description = training.getDescription();
                this.techStacks = training.getTrainingTechStacks()
                        .stream()
                        .map(tts -> tts.getTechStack())
                        .toList();
            }
        }

        @Data
        public static class EtcDTO{
            private Integer id;
            private LocalDate startDate;
            private LocalDate endDate;
            private Boolean hasEndDate;
            private String title;
            private EtcType etcType;
            private String institutionName;
            private String description;

            public EtcDTO(Etc etc) {
                this.id = etc.getId();
                this.startDate = etc.getStartDate();
                this.endDate = etc.getEndDate();
                this.hasEndDate = etc.getHasEndDate();
                this.title = etc.getTitle();
                this.etcType = etc.getEtcType();
                this.institutionName = etc.getInstitutionName();
                this.description = etc.getDescription();
            }
        }
        public DTO(Resume resume) {
            this.id = resume.getId();
            this.title = resume.getTitle();

            // 이미지 처리
            String filename = resume.getPhotoUrl(); // 이미지 파일명(O) Base64 인코딩 문자열(X)
            if (filename != null && !filename.isEmpty()) {
                try {
                    byte[] imageBytes = Base64Util.readImageAsByteArray(filename);
                    String mimeType = filename.endsWith(".png") ? "png" : "jpeg";
                    this.photoUrl = Base64Util.encodeAsString(imageBytes, mimeType);
                } catch (Exception e) {
                    this.photoUrl = null; // 이미지 인코딩 실패 시 null 처리 또는 기본 이미지
                }
            } else {
                this.photoUrl = null;
            }

            this.summary = resume.getSummary();
            this.positionType = resume.getPositionType();
            this.selfIntroduction = resume.getSelfIntroduction();
            this.createdAt = resume.getCreatedAt().toString();
            this.updatedAt = resume.getUpdatedAt().toString();
            this.resumeTechStacks = resume.getResumeTechStacks()
                    .stream()
                    .map(rt -> rt.getTechStack())
                    .toList();
            this.educations = resume.getEducations().stream().map(education -> new EducationDTO(education)).toList();
            this.projects = resume.getProjects().stream().map(project -> new ProjectDTO(project)).toList();
            this.experiences = resume.getExperiences().stream().map(experience -> new ExperienceDTO(experience)).toList();
            this.links = resume.getLinks().stream().map(link -> new LinkDTO(link)).toList();
            this.trainings = resume.getTrainings().stream().map(train -> new TrainingDTO(train)).toList();
            this.etcs = resume.getEtcs().stream().map(etc -> new EtcDTO(etc)).toList();
        }
    }

    // 이력서 목록
    @Data
    public static class ListDTO{
        private List<ItemDTO> resumes;

        public ListDTO(List<Resume> resumes) {
            this.resumes = resumes
                    .stream()
                    .map(resume -> new ItemDTO(resume))
                    .toList();
        }

        @Data
        public static class ItemDTO{
            private Integer id;
            private String title;
            private String updatedAt;

            public ItemDTO(Resume resume) {
                this.id = resume.getId();
                this.title = resume.getTitle();
                this.updatedAt = resume.getUpdatedAt()!= null? resume.getUpdatedAt().toString().substring(0, 16) : resume.getCreatedAt().toString().substring(0,16);
            }
        }
    }

    // 이력서 저장시 유저정보 + 선택지
    @Data
    public static class SaveDTO {
        private String name;
        private String email;
        private Integer birthDate; // 생년만 (e.g. 2000)
        private String contactNumber;
        //private List<CareerLevelDTO> careerLevels;
        private List<EducationLevelDTO> educationLevels;
        private List<EtcTypeDTO> etcTypes;
        private List<String> positionTypes;
        private List<String> techStacks;

        @Data
        public static class EducationLevelDTO {
            private String name;
            private String label;

            public EducationLevelDTO(EducationLevel educationLevel) {
                this.name = educationLevel.name();
                this.label = educationLevel.label;
            }
        }

        @Data
        public static class EtcTypeDTO {
            private String name;
            private String label;

            public EtcTypeDTO(EtcType etcType) {
                this.name = etcType.name();
                this.label = etcType.label;
            }
        }


        public SaveDTO(User user, List<EducationLevel> educationLevels,
                       List<EtcType> etcTypes, List<String> positionTypes, List<String> techStacks) {
            this.name = user.getName();
            this.email = user.getEmail();
            this.birthDate = user.getBirthDate().getYear();
            this.contactNumber = user.getContactNumber();
            //this.careerLevels = careerLevels;
            this.educationLevels = educationLevels
                    .stream()
                    .map(educationLevel -> new EducationLevelDTO(educationLevel))
                    .toList();
            this.etcTypes = etcTypes
                    .stream()
                    .map(etcType -> new EtcTypeDTO(etcType))
                    .toList();
            this.positionTypes = positionTypes;
            this.techStacks = techStacks;
        }
    }

    // 이력서 수정시 유저정보 + 이력서 + 선택지
    @Data
    public static class UpdateDTO {
        private String name;
        private String email;
        private Integer birthDate; // 생년만 (e.g. 2000)
        private String contactNumber;
        //private List<CareerLevel> careerLevels;
        private List<EducationLevelDTO> educationLevels;
        private List<EtcTypeDTO> etcTypes;
        private List<String> positionTypes;
        private List<String> techStacks;
        private DTO resumeDTO;

        @Data
        public static class EducationLevelDTO {
            private String name;
            private String label;

            public EducationLevelDTO(EducationLevel educationLevel) {
                this.name = educationLevel.name();
                this.label = educationLevel.label;
            }
        }

        @Data
        public static class EtcTypeDTO {
            private String name;
            private String label;

            public EtcTypeDTO(EtcType etcType) {
                this.name = etcType.name();
                this.label = etcType.label;
            }
        }

        public UpdateDTO(User user, List<EducationLevel> educationLevels,
                         List<EtcType> etcTypes, List<String> positionTypes, List<String> techStacks, DTO resumeDTO) {
            this.name = user.getName();
            this.email = user.getEmail();
            this.birthDate = user.getBirthDate().getYear();
            this.contactNumber = user.getContactNumber();
            //this.careerLevels = careerLevels;
            this.educationLevels = educationLevels
                    .stream()
                    .map(educationLevel -> new EducationLevelDTO(educationLevel))
                    .toList();
            this.etcTypes = etcTypes
                    .stream()
                    .map(etcType -> new EtcTypeDTO(etcType))
                    .toList();
            this.positionTypes = positionTypes;
            this.techStacks = techStacks;
            this.resumeDTO = resumeDTO;
        }
    }
}
