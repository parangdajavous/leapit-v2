package com.example.leapit.resume;

import com.example.leapit.common.enums.EducationLevel;
import com.example.leapit.resume.education.Education;
import com.example.leapit.resume.etc.Etc;
import com.example.leapit.resume.experience.Experience;
import com.example.leapit.resume.experience.techstack.ExperienceTechStack;
import com.example.leapit.resume.link.Link;
import com.example.leapit.resume.project.Project;
import com.example.leapit.resume.project.techstack.ProjectTechStack;
import com.example.leapit.resume.techstack.ResumeTechStack;
import com.example.leapit.resume.training.Training;
import com.example.leapit.resume.training.techstack.TrainingTechStack;
import com.example.leapit.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class ResumeRequest {
    @Data
    public static class SaveDTO {
        @NotEmpty(message = "제목을 입력해주세요.")
        private String title;
        private String photoUrl; // Base64로 인코딩된 문자열을 frontend로부터 받아온다.
        private String photoFileName; // 원본 파일명
        private String summary;
        @NotEmpty(message = "직무를 선택해주세요.")
        private String positionType;
        @NotEmpty(message = "기술스택을 선택해주세요.")
        private List<String> resumeTechStacks;
        @Valid
        private List<LinkDTO> links;
        @Valid
        private List<EducationDTO> educations;
        @Valid
        private List<ExperienceDTO> experiences;
        @Valid
        private List<ProjectDTO> projects;
        @Valid
        private List<TrainingDTO> trainings;
        @Valid
        private List<EtcDTO> etcs;
        private String selfIntroduction;

        @Data
        public static class LinkDTO {
            @NotEmpty(message = "링크 이름을 입력해주세요.")
            private String title;
            @NotEmpty(message = "링크를 입력해주세요.")
            private String url;
        }

        @Data
        public static class EducationDTO {
            @NotNull(message = "졸업일을 선택해주세요.")
            private LocalDate graduationDate;
            private Boolean isDropout;
            @NotEmpty(message ="학력구분을 선택해주세요.")
            private EducationLevel educationLevel;
            @NotEmpty(message ="학교명을 입력해주세요.")
            private String schoolName;
            private String major;
            private BigDecimal gpa;
            private BigDecimal gpaScale;
        }

        @Data
        public static class ExperienceDTO {
            @NotNull(message = "시작일을 선택해주세요.")
            private LocalDate startDate;
            private LocalDate endDate;
            private Boolean isEmployed;
            @NotEmpty(message = "회사명을 입력해주세요.")
            private String companyName;
            private String summary;
            private String position;
            @NotEmpty(message = "주요 업무를 입력해주세요.")
            private String responsibility;
            @NotEmpty(message = "기술 스택을 1개 이상 선택해주세요.")
            @Valid
            private List<String> techStacks;
        }


        @Data
        public static class ProjectDTO {
            @NotNull(message = "시작일을 선택해주세요.")
            private LocalDate startDate;
            private LocalDate endDate;
            private Boolean isOngoing;
            @NotEmpty(message = "제목을 입력해주세요.")
            private String title;
            private String summary;
            @NotEmpty(message = "설명을 입력해주세요.")
            private String description;
            private String repositoryUrl;
            @NotEmpty(message = "기술 스택을 1개 이상 선택해주세요.")
            @Valid
            private List<String> techStacks;
        }

        @Data
        public static class TrainingDTO {
            @NotNull(message = "시작일을 선택해주세요.")
            private LocalDate startDate;
            private LocalDate endDate;
            private Boolean isOngoing;
            @NotEmpty(message = "교육 과정명을 입력해주세요.")
            private String courseName;
            @NotEmpty(message = "기관명을 입력해주세요.")
            private String institutionName;
            @NotEmpty(message = "상세 설명을 입력해주세요.")
            private String description;
            @NotEmpty(message = "기술 스택을 1개 이상 선택해주세요.")
            @Valid
            private List<String> techStacks;
        }

        @Data
        public static class EtcDTO {
            @NotNull(message = "시작일을 선택해주세요.")
            private LocalDate startDate;
            private LocalDate endDate;
            private Boolean hasEndDate;
            @NotEmpty(message = "활동명/제목을 입력해주세요.")
            private String title;
            @NotEmpty(message = "활동 구분을 선택해주세요.")
            private String etcType;
            @NotEmpty(message = "기관명을 입력해주세요.")
            private String institutionName;
            @NotEmpty(message = "상세내용/점수를 입력해주세요.")
            private String description;
        }

        public Resume toEntity(User user){
            Resume resume = Resume.builder()
                    .title(title)
                    .photoUrl(photoUrl)
                    .summary(summary)
                    .positionType(positionType)
                    .selfIntroduction(selfIntroduction)
                    .user(user)
                    .build();

            // 기술스택
            if(resumeTechStacks != null){
                for (String techStack : resumeTechStacks){
                    ResumeTechStack rts = ResumeTechStack.builder()
                            .resume(resume)
                            .techStack(techStack)
                            .build();
                    resume.getResumeTechStacks().add(rts);
                }
            }

            // 링크
            if (links != null){
                for (LinkDTO linkDTO : links){
                    Link link = Link.builder()
                            .title(linkDTO.getTitle())
                            .url(linkDTO.getUrl())
                            .resume(resume)
                            .build();
                    resume.getLinks().add(link);
                }
            }

            // 학력
            if (educations != null){
                for (EducationDTO educationDTO : educations){
                    Education education = Education.builder()
                            .graduationDate(educationDTO.getGraduationDate())
                            .isDropout(educationDTO.getIsDropout())
                            .educationLevel(educationDTO.getEducationLevel())
                            .schoolName(educationDTO.getSchoolName())
                            .major(educationDTO.getMajor())
                            .gpa(educationDTO.getGpa())
                            .gpaScale(educationDTO.getGpaScale())
                            .resume(resume)
                            .build();
                    resume.getEducations().add(education);
                }
            }

            // 경력
            if (experiences != null) {
                for (ExperienceDTO experienceDTO : experiences) {
                    Experience experience = Experience.builder()
                            .startDate(experienceDTO.getStartDate())
                            .endDate(experienceDTO.getEndDate())
                            .isEmployed(experienceDTO.getIsEmployed())
                            .companyName(experienceDTO.getCompanyName())
                            .summary(experienceDTO.getSummary())
                            .position(experienceDTO.getPosition())
                            .responsibility(experienceDTO.getResponsibility())
                            .resume(resume)
                            .build();

                    if (experienceDTO.getTechStacks() != null) {
                        for (String techStack : experienceDTO.getTechStacks()) {
                            ExperienceTechStack ets = ExperienceTechStack.builder()
                                    .techStack(techStack)
                                    .experience(experience)
                                    .build();
                            experience.getExperienceTechStacks().add(ets);
                        }
                    }
                    resume.getExperiences().add(experience);
                }
            }

            // 프로젝트
            if (projects != null) {
                for (ProjectDTO projectDTO : projects) {
                    Project project = Project.builder()
                            .startDate(projectDTO.getStartDate())
                            .endDate(projectDTO.getEndDate())
                            .isOngoing(projectDTO.getIsOngoing())
                            .title(projectDTO.getTitle())
                            .summary(projectDTO.getSummary())
                            .description(projectDTO.getDescription())
                            .repositoryUrl(projectDTO.getRepositoryUrl())
                            .resume(resume)
                            .build();

                    if (projectDTO.getTechStacks() != null) {
                        for (String techStack : projectDTO.getTechStacks()) {
                            ProjectTechStack pts = ProjectTechStack.builder()
                                    .techStack(techStack)
                                    .project(project)
                                    .build();
                            project.getProjectTechStacks().add(pts);
                        }
                    }
                    resume.getProjects().add(project);
                }
            }
            // 교육이력
            if (trainings != null) {
                for (TrainingDTO trainingDTO : trainings) {
                    Training training = Training.builder()
                            .startDate(trainingDTO.getStartDate())
                            .endDate(trainingDTO.getEndDate())
                            .isOngoing(trainingDTO.getIsOngoing())
                            .courseName(trainingDTO.getCourseName())
                            .institutionName(trainingDTO.getInstitutionName())
                            .description(trainingDTO.getDescription())
                            .resume(resume)
                            .build();

                    if (trainingDTO.getTechStacks() != null) {
                        for (String techStack : trainingDTO.getTechStacks()) {
                            TrainingTechStack tts = TrainingTechStack.builder()
                                    .techStack(techStack)
                                    .training(training)
                                    .build();
                            training.getTrainingTechStacks().add(tts);
                        }
                    }
                    resume.getTrainings().add(training);
                }
            }

            // 기타사항
            if (etcs != null) {
                for (EtcDTO etcDTO : etcs) {
                    Etc etc = Etc.builder()
                            .startDate(etcDTO.getStartDate())
                            .endDate(etcDTO.getEndDate())
                            .hasEndDate(etcDTO.getHasEndDate())
                            .title(etcDTO.getTitle())
                            .etcType(etcDTO.getEtcType())
                            .institutionName(etcDTO.getInstitutionName())
                            .description(etcDTO.getDescription())
                            .resume(resume)
                            .build();
                    resume.getEtcs().add(etc);
                }
            }
            return resume;
        }
    }
}
