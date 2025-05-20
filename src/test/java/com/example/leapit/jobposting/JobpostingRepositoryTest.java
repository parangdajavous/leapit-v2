package com.example.leapit.jobposting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

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
}
