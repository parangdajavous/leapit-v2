package com.example.leapit.application;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ApplicationRepository {
    private final EntityManager em;

    public List<Application> findAllByResumeId(Integer resumeId) {
        Query query = em.createQuery("Select a from Application a where a.resume.id = :resumeId");
        query.setParameter("resumeId", resumeId);
        return query.getResultList();
    }
}
