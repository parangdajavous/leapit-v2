package com.example.leapit.jobposting;

import com.example.leapit.common.enums.CareerLevel;
import com.example.leapit.common.enums.EducationLevel;
import com.example.leapit.jobposting.techstack.JobPostingTechStack;
import com.example.leapit.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.*;

@Import(JobPostingRepository.class)
@DataJpaTest
public class JobPostingRepositoryTest {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private EntityManager em;

    // 전체 공고 조회
    @Test
    public void find_all_by_company_user_id() {
        // given
        Integer companyUserId = 7;

        // when
        List<JobPostingResponse.ListDTO> result = jobPostingRepository.findAllByCompanyUserId(companyUserId);

        // eye
        System.out.println("==================전체 공고===================");
        for (JobPostingResponse.ListDTO dto : result) {
            System.out.println("공고 ID: " + dto.getJobPostingId());
            System.out.println("공고명: " + dto.getTitle());
            System.out.println("=====================================");
        }
    }

    // 진행중인 공고 조회
    @Test
    public void find_open_by_company_user_id() {
        // given
        Integer companyUserId = 7;

        // when
        List<JobPostingResponse.ListDTO> result = jobPostingRepository.findOpenByCompanyUserId(companyUserId);

        // eye
        System.out.println("==================진행중인 공고===================");
        for (JobPostingResponse.ListDTO dto : result) {
            System.out.println("공고 ID: " + dto.getJobPostingId());
            System.out.println("공고명: " + dto.getTitle());
            System.out.println("=====================================");
        }
    }

    // 마감된 공고 조회
    @Test
    public void find_closed_by_company_user_id() {
        // given
        Integer companyUserId = 7;

        // when
        List<JobPostingResponse.ListDTO> result = jobPostingRepository.findClosedByCompanyUserId(companyUserId); // ← 핵심 변경

        // eye
        System.out.println("==================마감된 공고===================");
        for (JobPostingResponse.ListDTO dto : result) {
            System.out.println("공고 ID: " + dto.getJobPostingId());
            System.out.println("공고명: " + dto.getTitle());
            System.out.println("=====================================");
        }
    }

    @Test
    public void findTop8PopularJoinTechStacks_test() {
        // when
        List<Object[]> result = jobPostingRepository.findTop8PopularJoinTechStacks();

        // 공고 ID 기준으로 그룹핑
        Map<Integer, JobPosting> postingMap = new LinkedHashMap<>(); // 순서 유지
        Map<Integer, List<JobPostingTechStack>> stackMap = new HashMap<>();

        for (Object[] row : result) {
            JobPosting jp = (JobPosting) row[0];
            JobPostingTechStack stack = (JobPostingTechStack) row[1];

            // 공고 저장
            postingMap.putIfAbsent(jp.getId(), jp);

            // 기술스택 누적
            if (stack != null) {
                stackMap.computeIfAbsent(jp.getId(), k -> new ArrayList<>()).add(stack);
            }
        }

        // eye
        System.out.println("===== 인기 공고 + 기술스택 목록 =====");
        for (Map.Entry<Integer, JobPosting> entry : postingMap.entrySet()) {
            JobPosting jp = entry.getValue();
            System.out.println("공고명: " + jp.getTitle());

            List<JobPostingTechStack> techStacks = stackMap.getOrDefault(jp.getId(), new ArrayList<>());
            if (techStacks.isEmpty()) {
                System.out.println(" - 기술스택 없음");
            } else {
                for (JobPostingTechStack ts : techStacks) {
                    System.out.println(" - 기술스택: " + ts.getTechStack());
                }
            }
        }
    }

    @Test
    public void findTop8Popular_test() {
        // when
        List<Integer> ids = jobPostingRepository.findTop8Popular();

        // eye
        System.out.println("인기 공고 ID 목록: " + ids);
    }

    @Test
    public void findByRegion_test() {
        // given
        Integer jobPostingId = 1;

        // when
        String region = jobPostingRepository.findByRegion(jobPostingId);

        // eye
        System.out.println("시(region): " + region);
    }

    @Test
    public void findBySubRegion_test() {
        // given
        Integer jobPostingId = 1;

        // when
        String subRegion = jobPostingRepository.findBySubRegion(jobPostingId);

        // eye
        System.out.println("구(subRegion): " + subRegion);
    }

    @Test
    public void findTop3Recent_test() {

        // when
        List<JobPosting> result = jobPostingRepository.findTop3Recent();

        // eye
        System.out.println("===== 최신 공고 3개 =====");
        for (JobPosting jp : result) {
            System.out.println("공고명: " + jp.getTitle() + ", 등록일: " + jp.getCreatedAt());
        }
    }

    @Test
    public void delete_test() {
        // given
        Integer jobPostingId = 1;

        // when
        jobPostingRepository.deleteById(jobPostingId);

        // eye
        JobPosting deleted = jobPostingRepository.findById(jobPostingId).orElse(null);

        System.out.println("=========== 채용공고 삭제 결과 확인 ===========");
        if (deleted == null) {
            System.out.println("삭제 성공! 해당 ID의 채용공고는 존재하지 않음");
        } else {
            System.out.println("삭제 실패! 아직 존재함 → ID: " + deleted.getId());
        }
        System.out.println("===============================================");
        //=========== 채용공고 삭제 결과 확인 ===========
        //삭제 성공! 해당 ID의 채용공고는 존재하지 않음
        //===============================================
    }


    @Test
    public void find_by_id_test() {
        // given
        Integer jobPostingId = 1; // 실제 존재하는 ID로 바꿔야 함

        // when
        JobPosting jp = jobPostingRepository.findById(jobPostingId).orElseThrow();

        // eye
        System.out.println("=========== 채용공고 상세조회 결과 ===========");
        System.out.println("ID: " + jp.getId());
        System.out.println("작성자 ID: " + jp.getUser().getId());
        System.out.println("제목: " + jp.getTitle());
        System.out.println("직무: " + jp.getPositionType());
        System.out.println("최소 경력: " + jp.getMinCareerLevel());
        System.out.println("최대 경력: " + jp.getMaxCareerLevel());
        System.out.println("학력: " + jp.getEducationLevel());
        System.out.println("지역 ID: " + jp.getAddressRegionId());
        System.out.println("세부 지역 ID: " + jp.getAddressSubRegionId());
        System.out.println("주소 상세: " + jp.getAddressDetail());
        System.out.println("서비스 소개: " + jp.getServiceIntro());
        System.out.println("마감일: " + jp.getDeadline());
        System.out.println("담당 업무: " + jp.getResponsibility());
        System.out.println("자격 요건: " + jp.getQualification());
        System.out.println("우대 사항: " + jp.getPreference());
        System.out.println("복지: " + jp.getBenefit());
        System.out.println("추가 정보: " + jp.getAdditionalInfo());
        System.out.println("조회수: " + jp.getViewCount());
        System.out.println("기술스택 목록:");
        for (JobPostingTechStack techStack : jp.getJobPostingTechStacks()) {
            System.out.println("- " + techStack.getTechStack());
        }
        System.out.println("==============================================");
        //=========== 채용공고 상세조회 결과 ===========
        //ID: 1
        //작성자 ID: 6
        //제목: 시니어 백엔드 개발자 채용
        //직무: 백엔드
        //최소 경력: YEAR_5
        //최대 경력: OVER_10
        //학력: HIGH_SCHOOL
        //지역 ID: 1
        //세부 지역 ID: 1
        //주소 상세: 강남대로 123
        //서비스 소개: 대용량 트래픽 처리 기반 백엔드 플랫폼 개발
        //마감일: 2025-06-30
        //담당 업무: 마이크로서비스 아키텍처 기반 시스템 설계 및 운영
        //자격 요건: Java, Spring 기반 개발 경험 필수
        //우대 사항: AWS 경험자 우대
        //복지: 탄력 근무제, 점심 제공
        //추가 정보: null
        //조회수: 3
        //기술스택 목록:
        //- Python
        //- Java
        //- React
    }

    @Test
    public void save_test() {
        // given
        User companyUser = em.find(User.class, 7); // 실제 존재하는 기업 유저 ID
        List<String> techStackCodes = List.of("SPRING", "JAVA", "AWS");

        JobPosting jobPosting = JobPosting.builder()
                .user(companyUser)
                .title("백엔드 개발자 모집")
                .positionType("백엔드")
                .minCareerLevel(CareerLevel.YEAR_2)
                .maxCareerLevel(CareerLevel.YEAR_5)
                .educationLevel(EducationLevel.BACHELOR)
                .addressRegionId(1)
                .addressSubRegionId(1)
                .addressDetail("서울시 강남구")
                .serviceIntro("우리는 AI 기반 솔루션을 제공합니다.")
                .deadline(LocalDate.of(2025, 6, 30))
                .responsibility("Spring Boot 기반 API 개발 및 유지보수")
                .qualification("Java/Spring에 대한 이해, 2년 이상 경력")
                .preference("AWS 경험자 우대")
                .benefit("자율 출퇴근, 점심 제공")
                .additionalInfo("포트폴리오 제출 필수")
                .build();

        // 연관된 기술스택 추가
        for (String code : techStackCodes) {
            JobPostingTechStack techStack = JobPostingTechStack.builder()
                    .jobPosting(jobPosting)
                    .techStack(code)
                    .build();
            jobPosting.getJobPostingTechStacks().add(techStack);
        }

        // when
        JobPosting saved = jobPostingRepository.save(jobPosting);

        // eye
        System.out.println("=========== 채용공고 저장 결과 ===========");
        System.out.println("ID: " + saved.getId());
        System.out.println("작성자 ID: " + saved.getUser().getId());
        System.out.println("제목: " + saved.getTitle());
        System.out.println("직무: " + saved.getPositionType());
        System.out.println("최소 경력: " + saved.getMinCareerLevel());
        System.out.println("최대 경력: " + saved.getMaxCareerLevel());
        System.out.println("학력: " + saved.getEducationLevel());
        System.out.println("지역 ID: " + saved.getAddressRegionId());
        System.out.println("세부 지역 ID: " + saved.getAddressSubRegionId());
        System.out.println("주소 상세: " + saved.getAddressDetail());
        System.out.println("서비스 소개: " + saved.getServiceIntro());
        System.out.println("마감일: " + saved.getDeadline());
        System.out.println("담당 업무: " + saved.getResponsibility());
        System.out.println("자격 요건: " + saved.getQualification());
        System.out.println("우대 사항: " + saved.getPreference());
        System.out.println("복지: " + saved.getBenefit());
        System.out.println("추가 정보: " + saved.getAdditionalInfo());
        System.out.println("조회수: " + saved.getViewCount());
        System.out.println("기술스택 목록:");
        for (JobPostingTechStack techStack : saved.getJobPostingTechStacks()) {
            System.out.println("- " + techStack.getTechStack());
        }
        System.out.println("==========================================");
        // =========== 채용공고 저장 결과 ===========
        //ID: 13
        //작성자 ID: 7
        //제목: 백엔드 개발자 모집
        //직무: 백엔드
        //최소 경력: YEAR_2
        //최대 경력: YEAR_5
        //학력: BACHELOR
        //지역 ID: 1
        //세부 지역 ID: 1
        //주소 상세: 서울시 강남구
        //서비스 소개: 우리는 AI 기반 솔루션을 제공합니다.
        //마감일: 2025-06-30
        //담당 업무: Spring Boot 기반 API 개발 및 유지보수
        //자격 요건: Java/Spring에 대한 이해, 2년 이상 경력
        //우대 사항: AWS 경험자 우대
        //복지: 자율 출퇴근, 점심 제공
        //추가 정보: 포트폴리오 제출 필수
        //조회수: 0
        //기술스택 목록:
        //- SPRING
        //- JAVA
        //- AWS
        //==========================================
    }


    // 기업 - 채용공고 리스트
    @Test
    public void find_by_id_join_job_posting() {
        // given
        Integer companyUserId = 7;

        // when
        List<JobPostingResponse.companyListDTO> openPostings =
                jobPostingRepository.findOpenJobPostingByCompanyUserId(companyUserId);

        List<JobPostingResponse.companyListDTO> closedPostings =
                jobPostingRepository.findClosedJobPostingByCompanyUserId(companyUserId);

        // eye
        System.out.println("================= 진행중 공고 =================");
        for (JobPostingResponse.companyListDTO dto : openPostings) {
            System.out.println("공고 ID: " + dto.getJobPostingId());
            System.out.println("공고 제목: " + dto.getTitle());
            System.out.println("마감일: " + dto.getDeadLine());
            System.out.println("--------------------------------------------");
        }

        System.out.println("================= 마감된 공고 =================");
        for (JobPostingResponse.companyListDTO dto : closedPostings) {
            System.out.println("공고 ID: " + dto.getJobPostingId());
            System.out.println("공고 제목: " + dto.getTitle());
            System.out.println("마감일: " + dto.getDeadLine());
            System.out.println("--------------------------------------------");
        }
        //================= 진행중 공고 =================
        //공고 ID: 3
        //공고 제목: 데이터 엔지니어 채용
        //마감일: 2025-07-20
        //--------------------------------------------
        //공고 ID: 9
        //공고 제목: iOS 앱 개발자 구인
        //마감일: 2025-07-31
        //--------------------------------------------
        //공고 ID: 12
        //공고 제목: QA 엔지니어 모집
        //마감일: 2025-07-20
        //--------------------------------------------

        //================= 마감된 공고 =================
        //공고 ID: 2
        //공고 제목: 프론트엔드 개발자 모집
        //마감일: 2025-05-20
        //--------------------------------------------
        //공고 ID: 4
        //공고 제목: 주니어 데이터 엔지니어 모집
        //마감일: 2025-05-20
        //--------------------------------------------
        //공고 ID: 5
        //공고 제목: 마감된 개발자 모집
        //마감일: 2025-03-20
        //--------------------------------------------
    }

}
