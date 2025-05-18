package com.example.leapit.jobposting;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JobPostingRepository {
    private final EntityManager em;

    // 채용공고 저장
    public JobPosting save(JobPosting jobPosting) {
        em.persist(jobPosting);
        return jobPosting;
    }
}
