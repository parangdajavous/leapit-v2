package com.example.leapit.companyinfo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CompanyInfoRepository {
    private final EntityManager em;

    public Optional<CompanyInfo> findByUserId(Integer userId) {
        try {
            CompanyInfo result = em.createQuery(
                            "select c from CompanyInfo c where c.user.id = :userId", CompanyInfo.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
