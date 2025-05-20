package com.example.leapit.resume.experience;

import com.example.leapit.resume.Resume;
import com.example.leapit.resume.ResumeRequest;
import com.example.leapit.resume.experience.techstack.ExperienceTechStackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final ExperienceTechStackService experienceTechStackService;

    @Transactional
    public void update(Resume resumePS, @Valid List<ResumeRequest.UpdateDTO.ExperienceDTO> experienceDTOs) {
        List<Experience> experiences = resumePS.getExperiences();

        // 요청에서 id가 있는 DTO들만 Map으로 변환
        Map<Integer, ResumeRequest.UpdateDTO.ExperienceDTO> dtoMap = experienceDTOs.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toMap(ResumeRequest.UpdateDTO.ExperienceDTO::getId, dto -> dto));

        // 1. 기존 Experience 리스트 돌면서 수정 or 삭제
        Iterator<Experience> iterator = experiences.iterator();
        while (iterator.hasNext()) {
            Experience experience = iterator.next();
            ResumeRequest.UpdateDTO.ExperienceDTO dto = dtoMap.remove(experience.getId());

            if (dto != null) {
                // 수정 대상이면 update
                experience.update(
                        dto.getStartDate(),
                        dto.getEndDate(),
                        dto.getIsEmployed(),
                        dto.getCompanyName(),
                        dto.getSummary(),
                        dto.getPosition(),
                        dto.getResponsibility()
                );
                experienceTechStackService.update(experience, dto.getTechStacks());
            } else {
                // 요청에 없으면 삭제
                iterator.remove();
            }
        }

        // 2. 요청 데이터 중 id가 없는 것은 신규 추가
        for (ResumeRequest.UpdateDTO.ExperienceDTO dto : experienceDTOs) {
            if (dto.getId() == null) {
                Experience newExperience = Experience.builder()
                        .resume(resumePS)
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .isEmployed(dto.getIsEmployed())
                        .companyName(dto.getCompanyName())
                        .summary(dto.getSummary())
                        .position(dto.getPosition())
                        .responsibility(dto.getResponsibility())
                        .build();
                experiences.add(newExperience);

                experienceTechStackService.update(newExperience, dto.getTechStacks());
            }
        }
    }
}
