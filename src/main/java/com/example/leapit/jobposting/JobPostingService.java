package com.example.leapit.jobposting;
import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit.common.enums.CareerLevel;
import com.example.leapit.common.enums.EducationLevel;
import com.example.leapit.common.positiontype.PositionTypeRepository;
import com.example.leapit.common.region.Region;
import com.example.leapit.common.region.RegionRepository;
import com.example.leapit.common.region.SubRegion;
import com.example.leapit.common.techstack.TechStackRepository;
import com.example.leapit.companyinfo.CompanyInfo;
import com.example.leapit.companyinfo.CompanyInfoRepository;
import com.example.leapit.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.leapit.common.region.RegionResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    private final TechStackRepository techStackRepository;
    private final RegionRepository regionRepository;
    private final PositionTypeRepository positionTypeRepository;
    private final CompanyInfoRepository companyInfoRepository;


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

        // 6. 학력 (enum)
        List<EducationLevel> educationLevels = List.of(EducationLevel.values());

        // 7. 최종 응답 DTO 생성
        return new JobPostingResponse.SaveDTO(positionTypes, techStacks, regionDTOs, careerLevels, educationLevels);
    }

    // 기업 채용공고 상세보기
    @Transactional
    public JobPostingResponse.DTO getDetailCompany(Integer id) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("해당 채용공고를 찾을 수 없습니다."));
        return new JobPostingResponse.DTO(jobPosting);
    }

    // 구직자 채용공고 상세보기
    @Transactional
    public JobPostingResponse.DetailPersonalDTO getDetailPersonal(Integer jobPostingId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new ExceptionApi404("해당 채용공고를 찾을 수 없습니다."));

        // 채용공고를 작성한 기업 유저
        Integer companyUserId = jobPosting.getUser().getId();

        // 기업 유저의 회사 정보 조회
        CompanyInfo companyInfo = companyInfoRepository.findByUserId(companyUserId)
                .orElseThrow(() -> new ExceptionApi404("해당 기업의 정보가 존재하지 않습니다."));

        // DTO 구성
        JobPostingResponse.DTO companyDTO = new JobPostingResponse.DTO(jobPosting);
        JobPostingResponse.DetailPersonalDTO.CompanyInfoDTO companyInfoDTO =
                new JobPostingResponse.DetailPersonalDTO.CompanyInfoDTO(companyInfo);

        return new JobPostingResponse.DetailPersonalDTO(companyDTO, companyInfoDTO);
    }

    // 채용공고 삭제
    @Transactional
    public void delete(Integer id, Integer sessionUser) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("해당 채용공고를 찾을 수 없습니다."));

        if (!jobPosting.getUser().getId().equals(sessionUser)) {
            throw new ExceptionApi403("삭제 권한이 없습니다.");
        }

        jobPostingRepository.deleteById(id);
    }

    // 공고현황 페이지(필터)
    public JobPostingResponse.FilteredListDTO getList(JobPostingRequest.FilterDTO reqDTO,Integer sessionUserId) {

        // 1. 포지션 타입 코드 리스트 (예: ["백엔드", "프론트엔드"])
        List<String> positionTypes = positionTypeRepository.findAll();

        // 2. 기술 스택 코드 리스트 (예: ["Java", "React"])
        List<String> techStacks = techStackRepository.findAll();

        // 3. 전체 지역 조회
        List<Region> regions = regionRepository.findAllRegion();

        // 4. 커리어 레벨 (enum)
        List<CareerLevel> careerLevels = List.of(CareerLevel.values());

        // 5. Region + SubRegion DTO 변환
        List<RegionResponse.DTO> regionDTOs = new ArrayList<>();
        for (Region region : regions) {
            List<SubRegion> subRegions = regionRepository.findAllSubRegionByRegionId(region.getId());
            RegionResponse.DTO regionDTO = new RegionResponse.DTO(region.getId(), region.getName(), subRegions);
            regionDTOs.add(regionDTO);
        }

        // 전체 공고목록 조회
        List<JobPostingResponse.ItemDTO> jobPostingList = jobPostingRepository.findAllByFilter(
                reqDTO.getRegionId(),
                reqDTO.getSubRegionId(),
                reqDTO.getCareerLevel(),
                reqDTO.getTechStackCode(),
                reqDTO.getSelectedPosition(),
                reqDTO.getSortType(),
                sessionUserId
        );

        JobPostingResponse.FilteredListDTO respDTO =
                new JobPostingResponse.FilteredListDTO(
                        positionTypes,
                        techStacks,
                        regionDTOs,
                        careerLevels,
                        jobPostingList
                );
        return respDTO;
    }
}
