package com.example.leapit.resume.project;

import com.example.leapit.resume.Resume;
import com.example.leapit.resume.ResumeRequest;
import com.example.leapit.resume.project.techstack.ProjectTechStackService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectTechStackService projectTechStackService;

    @Transactional
    public void update(Resume resumePS, @NotEmpty(message = "프로젝트는 필수항목입니다.") @Valid List<ResumeRequest.UpdateDTO.ProjectDTO> projectDTOs) {
        List<Project> projects = resumePS.getProjects();

        Map<Integer, ResumeRequest.UpdateDTO.ProjectDTO> dtoMap = projectDTOs.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toMap(ResumeRequest.UpdateDTO.ProjectDTO::getId, dto -> dto));

        Iterator<Project> iterator = projects.iterator();
        while (iterator.hasNext()) {
            Project project = iterator.next();
            ResumeRequest.UpdateDTO.ProjectDTO dto = dtoMap.remove(project.getId());

            if (dto != null) {
                project.update(
                        dto.getStartDate(), dto.getEndDate(), dto.getIsOngoing(),
                        dto.getTitle(), dto.getSummary(), dto.getDescription(), dto.getRepositoryUrl()
                );
                projectTechStackService.update(project, dto.getTechStacks());
            } else {
                iterator.remove();
            }
        }

        for (ResumeRequest.UpdateDTO.ProjectDTO dto : projectDTOs) {
            if (dto.getId() == null) {
                Project newProject = Project.builder()
                        .resume(resumePS)
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .isOngoing(dto.getIsOngoing())
                        .title(dto.getTitle())
                        .summary(dto.getSummary())
                        .description(dto.getDescription())
                        .repositoryUrl(dto.getRepositoryUrl())
                        .build();
                projects.add(newProject);

                projectTechStackService.update(newProject, dto.getTechStacks());
            }
        }
    }
}
