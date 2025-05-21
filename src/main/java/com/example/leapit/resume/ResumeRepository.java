package com.example.leapit.resume;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ResumeRepository {
    private final EntityManager em;

    public List<Resume> findAllByUserId(Integer userId) {
        Query query = em.createQuery("SELECT r FROM Resume r WHERE r.user.id = :userId", Resume.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public Optional<Resume> findById(Integer id) {
        return Optional.ofNullable(em.find(Resume.class, id));
    }

    public void deleteById(Integer id) {
        em.remove(em.find(Resume.class, id));
    }

    public Resume save(Resume resume) {
        em.persist(resume);
        return resume;
    }

    public List<Resume> findAllByUserIdJoinTechStacks(Integer userId) {
        String jpql = """
                    SELECT DISTINCT r
                    FROM Resume r
                    LEFT JOIN FETCH r.resumeTechStacks
                    WHERE r.user.id = :userId
                """;
        Query query = em.createQuery(jpql, Resume.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
