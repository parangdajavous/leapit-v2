package com.example.leapit.jobposting;

import com.example.leapit.jobposting.techstack.JobPostingTechStack;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.*;

@Import(JobPostingRepository.class)
@DataJpaTest
public class JobPostingRepositoryTest {

    @Autowired
    private JobPostingRepository jobPostingRepository;

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
}
