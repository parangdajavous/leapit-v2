package com.example.leapit.common.region;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RegionRepository {
    private final EntityManager em;

    public List<Region> findAllRegion() {
        return em.createQuery("SELECT r FROM Region r", Region.class).getResultList();
    }

    public List<SubRegion> findAllSubRegionByRegionId(Integer regionId) {
        return em.createQuery("SELECT sr FROM SubRegion sr WHERE sr.region.id = :regionId", SubRegion.class)
                .setParameter("regionId", regionId)
                .getResultList();
    }
}
