package com.example.leapit.application;

import com.example.leapit._core.error.ex.ExceptionApi400;
import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit.common.enums.PassStatus;
import com.example.leapit.common.enums.ViewStatus;
import com.example.leapit.companyinfo.CompanyInfo;
import com.example.leapit.companyinfo.CompanyInfoRepository;
import com.example.leapit.jobposting.JobPosting;
import com.example.leapit.jobposting.JobPostingRepository;
import com.example.leapit.jobposting.JobPostingResponse;
import com.example.leapit.resume.Resume;
import com.example.leapit.resume.ResumeRepository;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final CompanyInfoRepository companyInfoRepository;

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

    @Transactional
    public ApplicationResponse.UpdatePassDTO updatePass(Integer applicationId, ApplicationRequest.UpdatePassDTO reqDTO, Integer sessionUserId) {
        // 1. 지원서 존재 여부 확인
        Application applicationPS = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ExceptionApi404("해당 지원서는 존재하지 않습니다."));

        // 2. 권한 확인 (지원서의 채용공고 주인 == sessionUserId)
        if (!(applicationPS.getJobPosting().getUser().getId().equals(sessionUserId)))
            throw new ExceptionApi403("권한이 없습니다.");

        // 3. 합격 불합격 처리
        applicationPS.updatePassStatus(reqDTO.getPassStatus());

        return new ApplicationResponse.UpdatePassDTO(applicationPS.getId(), applicationPS.getPassStatus());
    }

    // 특정 채용공고에 대한 이력서 지원 화면
    public ApplicationResponse.ApplyDTO getApplyForm(Integer jobPostingId, Integer userId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new ExceptionApi404("해당 채용공고를 찾을 수 없습니다."));

        CompanyInfo companyInfo = companyInfoRepository.findByUserId(jobPosting.getUser().getId())
                .orElseThrow(() -> new ExceptionApi404("기업 정보를 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404("사용자 정보를 찾을 수 없습니다."));

        List<Resume> resumes = resumeRepository.findAllByUserIdJoinTechStacks(userId);

        return new ApplicationResponse.ApplyDTO(jobPosting, companyInfo, user, resumes);
    }

    //
    @Transactional
    public ApplicationResponse.SaveDTO save(ApplicationRequest.SaveDTO dto, Integer userId) {
        // 1. 중복 지원 방지
        if (applicationRepository.checkIfAlreadyApplied(userId, dto.getJobPostingId())) {
            throw new ExceptionApi400("이미 지원한 공고입니다.");
        }

        // 2. 이력서 조회 (본인 것만 허용)
        Resume resume = resumeRepository.findById(dto.getResumeId())
                .orElseThrow(() -> new ExceptionApi404("이력서를 찾을 수 없습니다."));
        if (!resume.getUser().getId().equals(userId)) {
            throw new ExceptionApi403("본인 이력서만 사용할 수 없습니다.");
        }

        // 3. 채용공고 조회
        JobPosting jobPosting = jobPostingRepository.findById(dto.getJobPostingId())
                .orElseThrow(() -> new ExceptionApi404("채용공고를 찾을 수 없습니다."));

        // 4. 지원서 엔티티 생성
        Application application = Application.builder()
                .resume(resume)
                .jobPosting(jobPosting)
                .appliedDate(LocalDate.now())
                .passStatus(PassStatus.WAITING)
                .viewStatus(ViewStatus.UNVIEWED)
                .build();

        // 5. 저장 및 응답 DTO 리턴
        Application applicationPS = applicationRepository.save(application);
        return new ApplicationResponse.SaveDTO(applicationPS);
    }
}