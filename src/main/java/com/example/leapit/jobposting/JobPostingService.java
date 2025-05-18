package com.example.leapit.jobposting;

import com.example.leapit.common.enums.CareerLevel;
import com.example.leapit.common.positiontype.PositionTypeRepository;
import com.example.leapit.common.region.Region;
import com.example.leapit.common.region.RegionRepository;
import com.example.leapit.common.region.SubRegion;
import com.example.leapit.common.techstack.TechStackRepository;
import com.example.leapit.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final TechStackRepository techStackRepository;
    private final RegionRepository regionRepository;
    private final PositionTypeRepository positionTypeRepository;


    // 채용공고 저장
    @Transactional
    public JobPostingResponse.DTO save(JobPostingRequest.SaveDTO reqDTO, User sessionUser) {
        // 1. 연관된 기술스택까지 포함된 JobPosting 생성
        JobPosting jobPosting = reqDTO.toEntity(sessionUser);

        // 2. 저장
        JobPosting jobPostingPS = jobPostingRepository.save(jobPosting);

        // 3. 응답용 DTO로 반환
        return new JobPostingResponse.DTO(jobPostingPS);
    }

    // 채용공고 저장 화면에 필요한 데이터 불러오기
    @Transactional
    public JobPostingResponse.SaveDTO getSaveForm() {
        // 1. 포지션 타입 코드 리스트 (예: ["백엔드", "프론트엔드"])
        List<String> positionTypes = positionTypeRepository.findAll();

        // 2. 기술 스택 코드 리스트 (예: ["Java", "React"])
        List<String> techStacks = techStackRepository.findAll();

        // 3. 전체 지역 조회
        List<Region> regions = regionRepository.findAllRegion();

        // 4. 커리어 레벨 (enum)
        List<CareerLevel> careerLevels = List.of(CareerLevel.values());

        // 5. Region + SubRegion DTO 변환
        List<JobPostingResponse.RegionDTO> regionDTOs = new ArrayList<>();
        for (Region region : regions) {
            List<SubRegion> subRegions = regionRepository.findAllSubRegionByRegionId(region.getId());
            JobPostingResponse.RegionDTO regionDTO = new JobPostingResponse.RegionDTO(region.getId(), region.getName(), subRegions);
            regionDTOs.add(regionDTO);
        }
        // 6. 최종 응답 DTO 생성
        return new JobPostingResponse.SaveDTO(positionTypes, techStacks, regionDTOs, careerLevels);
    }
}