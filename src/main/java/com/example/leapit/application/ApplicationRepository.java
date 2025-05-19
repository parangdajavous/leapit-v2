package com.example.leapit.application;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    // 지원 현황 통계
    public ApplicationResponse.StatusDTO findStatusByUserId(Integer userId) {
        String jpql = """
                    SELECT COUNT(a), 
                           SUM(CASE WHEN a.passStatus = 'PASS' THEN 1 ELSE 0 END), 
                           SUM(CASE WHEN a.passStatus = 'FAIL' THEN 1 ELSE 0 END)
                    FROM Application a
                    JOIN a.resume r
                    JOIN r.user u
                    WHERE u.id = :userId
                """;

        Object[] result = (Object[]) em.createQuery(jpql)
                .setParameter("userId", userId)
                .getSingleResult();

        return new ApplicationResponse.StatusDTO(
                (Long) result[0],
                (Long) result[1],
                (Long) result[2]
        );
    }

    // 지원 현황 목록
    public List<ApplicationResponse.ItemDTO> findItemsByUserId(Integer userId) {
        String jpql = """
                    SELECT\s
                        ci.companyName,\s
                        jp.title,\s
                        a.appliedDate,\s
                        r.id AS resumeId,\s
                        jp.id AS jobPostingId,
                        CASE\s
                        WHEN a.passStatus = 'PASS' THEN '합격'
                        WHEN a.passStatus = 'FAIL' THEN '불합격'
                        WHEN a.passStatus = 'WAITING' AND a.viewStatus = 'UNVIEWED' THEN '미열람'
                        WHEN a.passStatus = 'WAITING' AND a.viewStatus = 'VIEWED' THEN '미정'
                        END AS result
                    FROM Application a
                    JOIN a.resume r
                    JOIN r.user u
                    JOIN a.jobPosting jp
                    JOIN jp.user company
                    JOIN CompanyInfo ci ON ci.user.id = company.id
                    WHERE u.id = :userId
                    ORDER BY a.appliedDate DESC
                
                """;

        List<Object[]> resultList = em.createQuery(jpql, Object[].class)
                .setParameter("userId", userId)
                .getResultList();

        return resultList.stream()
                .map(row -> new ApplicationResponse.ItemDTO(
                        (String) row[0],
                        (String) row[1],
                        (LocalDate) row[2],
                        (Integer) row[3],
                        (Integer) row[4],
                        (String) row[5]
                ))
                .toList();
    }
}