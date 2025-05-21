package com.example.leapit.resume.techstack;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ResumeTechStackRepository {
    private final EntityManager em;

    public List<ResumeTechStack> findAllByResumeId(Integer resumeId) {
        return em.createQuery("SELECT rts FROM ResumeTechStack rts WHERE rts.resume.id = :resumeId", ResumeTechStack.class)
                .setParameter("resumeId", resumeId)
                .getResultList();
    }
}
