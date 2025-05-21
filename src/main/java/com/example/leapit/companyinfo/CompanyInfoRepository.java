package com.example.leapit.companyinfo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CompanyInfoRepository {
    private final EntityManager em;

    // 사용자 ID를 기준으로 해당 사용자의 기업 정보를 조회
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

    // 기업정보 등록
    public CompanyInfo save(CompanyInfo companyInfo) {
        em.persist(companyInfo);
        return companyInfo;
    }

    // 기업정보 조회
    public Optional<CompanyInfo> findById(Integer id) {
        CompanyInfo companyInfoPS = em.find(CompanyInfo.class, id);
        return Optional.ofNullable(companyInfoPS);
    }
}
