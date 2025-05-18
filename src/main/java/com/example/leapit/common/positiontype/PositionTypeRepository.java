package com.example.leapit.common.positiontype;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PositionTypeRepository {
    private final EntityManager em;

    public List<String> findAll() {
        return em.createQuery("SELECT pt.code FROM PositionType pt", String.class)
                .getResultList();
    }
}
