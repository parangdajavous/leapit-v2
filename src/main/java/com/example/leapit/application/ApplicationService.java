package com.example.leapit.application;

import com.example.leapit.jobposting.JobPostingRepository;
import com.example.leapit.jobposting.JobPostingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobPostingRepository jobPostingRepository;

    // 기업 지원자 현황 관리
    public ApplicationResponse.ApplicantPageDTO getApplicant(Integer companyUserId, ApplicationRequest.ApplicantListDTO reqDTO) {
        // 1. 전체 공고 리스트 조회
        List<JobPostingResponse.ListDTO> allPositions = jobPostingRepository.findAllByCompanyUserId(companyUserId);

        // 2. 진행중인 리스트 조회
        List<JobPostingResponse.ListDTO> openPositions = jobPostingRepository.findOpenByCompanyUserId(companyUserId);

        // 3. 마감된 리스트 조회
        List<JobPostingResponse.ListDTO> closedPositions = jobPostingRepository.findClosedByCompanyUserId(companyUserId);

        // 4. 지원받은 이력서 목록 조회(필터 : 북마크여부, 열람여부, 합불여부)
        List<ApplicationResponse.ApplicantListDTO> applicants = applicationRepository.findApplicantsByFilter(companyUserId, reqDTO.getJobPostingId(), reqDTO.getPassStatus(), reqDTO.getViewStatus(), reqDTO.getBookmarkStatus());

        // 5. pageDTO에 담아서 컨트롤러에 넘김
        ApplicationResponse.ApplicantPageDTO respDTO = new ApplicationResponse.ApplicantPageDTO(applicants, allPositions, openPositions, closedPositions);

        return respDTO;
    }

    // 개인 마이페이지 지원 현황 관리
    public ApplicationResponse.MyPageDTO getMyApplication(Integer userId) {
        // 지원 현황 통계
        ApplicationResponse.StatusDTO statusDTO = applicationRepository.findStatusByUserId(userId);
        if (statusDTO == null) {
            statusDTO = new ApplicationResponse.StatusDTO(0L, 0L, 0L);
        }

        // 지원 현황 목록 조회
        List<ApplicationResponse.MyPageDTO.ItemDTO> itemDTOs = applicationRepository.findItemsByUserId(userId);
        // respDTO에 담기
        ApplicationResponse.MyPageDTO respDTO = new ApplicationResponse.MyPageDTO(statusDTO, itemDTOs);
        return respDTO;
    }
}