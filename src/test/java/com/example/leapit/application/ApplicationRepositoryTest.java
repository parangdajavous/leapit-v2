package com.example.leapit.application;

import com.example.leapit.common.enums.BookmarkStatus;
import com.example.leapit.common.enums.PassStatus;
import com.example.leapit.common.enums.ViewStatus;
import com.example.leapit.jobposting.JobPosting;
import com.example.leapit.resume.Resume;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

@Import(ApplicationRepository.class)
@DataJpaTest
public class ApplicationRepositoryTest {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EntityManager em;

    // 지원받은 이력서 목록 조회
    @Test
    public void find_all_applicants_by_filter() {
        // given
        Integer companyUserId = 7; // 테스트용 company 유저 ID (DB에 존재하는 값이어야 함)
        Integer jobPostingId = null; // 전체 공고 대상으로 조회
        PassStatus passStatus = null; // 합불 조건 없이 전체
        ViewStatus viewStatus = null; // 열람 여부 전체
        BookmarkStatus bookmarkStatus = null; // 북마크 여부 전체

        // when
        List<ApplicationResponse.ApplicantListDTO> result = applicationRepository.findApplicantsByFilter(
                companyUserId,
                jobPostingId,
                passStatus,
                viewStatus,
                bookmarkStatus
        );

        // then
        System.out.println("================= 지원자 목록 =================");
        for (ApplicationResponse.ApplicantListDTO dto : result) {
            System.out.println("지원서 ID: " + dto.getApplicationId());
            System.out.println("이력서 ID: " + dto.getResumeId());
            System.out.println("지원자 이름: " + dto.getApplicantName());
            System.out.println("공고 제목: " + dto.getJobTitle());
            System.out.println("지원일자: " + dto.getAppliedDate());
            System.out.println("북마크 여부: " + dto.getBookmarkStatus());
            System.out.println("평가 상태: " + dto.getEvaluationStatus());
            System.out.println("--------------------------------------------");
        }
    }

    @Test
    public void findAllByResumeId_test_existingData() {
        // given
        Integer resumeId = 2;

        // when
        List<Application> applications = applicationRepository.findAllByResumeId(resumeId);

        // then
        System.out.println("조회된 지원서 수: " + applications.size());
        for (Application app : applications) {
            System.out.println("지원서 ID: " + app.getId() + ", 이력서 ID: " + app.getResume().getId());
        }
    }

    @Test
    public void find_status_by_user_id_test() {
        // given
        Integer userId = 1;

        // when
        ApplicationResponse.StatusDTO dto = applicationRepository.findStatusByUserId(userId);

        // eye
        System.out.println("=========통계 테스트=========");
        System.out.println("총 지원 수: " + dto.getTotal());
        System.out.println("합격 수: " + dto.getPassed());
        System.out.println("불합격 수: " + dto.getFailed());
        System.out.println("=========== 끝 ===========");
    }

    @Test
    public void find_items_by_user_id_test() {
        // given
        Integer userId = 1;

        // when
        List<ApplicationResponse.MyPageDTO.ItemDTO> applications = applicationRepository.findItemsByUserId(userId);

        // eye
        System.out.println("=========지원현황 테스트=========");
        for (ApplicationResponse.MyPageDTO.ItemDTO dto : applications) {
            System.out.println("회사명: " + dto.getCompanyName());
            System.out.println("공고 제목: " + dto.getJobTitle());
            System.out.println("지원일: " + dto.getAppliedDate());
            System.out.println("이력서 ID: " + dto.getResumeId());
            System.out.println("공고 ID: " + dto.getJobPostingId());
            System.out.println("결과: " + dto.getResult());
            System.out.println("=========== 끝 ===========");
        }
    }

    @Test
    public void save_test() {
        // given
        Integer resumeId = 2;
        Integer jobPostingId = 3;

        Resume resume = em.find(Resume.class, resumeId);
        JobPosting jobPosting = em.find(JobPosting.class, jobPostingId);

        Application application = Application.builder()
                .resume(resume)
                .jobPosting(jobPosting)
                .bookmark(BookmarkStatus.NOT_BOOKMARKED)
                .appliedDate(LocalDate.now())
                .passStatus(PassStatus.WAITING)
                .viewStatus(ViewStatus.UNVIEWED)
                .build();

        // when
        Application saved = applicationRepository.save(application);

        // eye
        System.out.println("================= 지원 저장 결과 =================");
        System.out.println("이력서 ID: " + saved.getResume().getId());
        System.out.println("채용공고 ID: " + saved.getJobPosting().getId());
        System.out.println("북마크 여부: "+saved.getBookmark());
        System.out.println("지원일시: " + saved.getAppliedDate());
        System.out.println("합격여부: " + saved.getPassStatus());
        System.out.println("열람여부: " + saved.getViewStatus());
        System.out.println("=================================================");
        //================= 지원 저장 결과 =================
        //이력서 ID: 2
        //채용공고 ID: 3
        //지원일시: 2025-05-21
        //지원일시: WAITING
        //지원일시: UNVIEWED
        //=================================================
    }
}