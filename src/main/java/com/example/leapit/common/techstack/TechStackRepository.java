package com.example.leapit.common.techstack;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TechStackRepository {
    private final EntityManager em;

    public List<String> findAll() {
        return em.createQuery("SELECT ts.code FROM TechStack ts ORDER BY ts.code", String.class)
                .getResultList();
    }
}
