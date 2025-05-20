package com.example.leapit.jobposting;

import com.example.leapit.common.enums.CareerLevel;
import com.example.leapit.common.enums.SortType;
import com.example.leapit.companyinfo.CompanyInfo;
import com.example.leapit.companyinfo.CompanyInfoResponse;
import com.example.leapit.jobposting.bookmark.JobPostingBookmark;
import com.example.leapit.jobposting.techstack.JobPostingTechStack;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JobPostingRepository {
    private final EntityManager em;

    // 채용공고 저장
    public JobPosting save(JobPosting jobPosting) {
        em.persist(jobPosting);
        return jobPosting;
    }

    // 아이디로 채용공고 찾기
    public Optional<JobPosting> findById(Integer id) {
        return Optional.ofNullable(em.find(JobPosting.class, id));
    }


    // COMPANY의 채용공고 & 해당 채용공고의 기술스택 조회
    public List<Object[]> findByUserIdJoinJobPostingTechStacks(Integer userId) {
        Query query = em.createQuery(
                "SELECT j, t FROM JobPosting j " +
                        "LEFT JOIN JobPostingTechStack t ON t.jobPosting.id = j.id " +
                        "WHERE j.user.id = :userId" + " ORDER BY j.deadline DESC "
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    // COMPANY가 채용중인 포지션 카운트 조회 (마감일이 오늘 이후인)
    public Long countByUserIdAndDeadlineAfter(Integer userId) {
        Query query = em.createQuery("SELECT COUNT(j) FROM JobPosting j WHERE j.user.id = :userId AND j.deadline >= CURRENT_DATE");
        query.setParameter("userId", userId);
        return (Long) query.getSingleResult();
    }

    // 아이디로 채용공고 삭제
    public void deleteById(Integer id) {
        em.remove(em.find(JobPosting.class, id));
    }

    // 전체 공고 조회
    public List<JobPostingResponse.ListDTO> findAllByCompanyUserId(Integer companyUserId) {
        String jpql = """
                    SELECT new com.example.leapit.jobposting.JobPostingResponse$ListDTO(
                        jpt.id,
                        jpt.title
                    )
                    FROM JobPosting jpt
                    JOIN jpt.user u
                    WHERE u.id = :companyUserId
                    ORDER BY jpt.createdAt DESC
                """;

        return em.createQuery(jpql, JobPostingResponse.ListDTO.class)
                .setParameter("companyUserId", companyUserId)
                .getResultList();
    }

    // 진행중인 공고 조회
    public List<JobPostingResponse.ListDTO> findOpenByCompanyUserId(Integer companyUserId) {
        String jpql = """
                    SELECT new com.example.leapit.jobposting.JobPostingResponse$ListDTO(
                        jpt.id,
                        jpt.title
                    )
                    FROM JobPosting jpt
                    JOIN jpt.user u
                    WHERE u.id = :companyUserId
                      AND jpt.deadline >= CURRENT_DATE
                    ORDER BY jpt.createdAt DESC
                """;

        return em.createQuery(jpql, JobPostingResponse.ListDTO.class)
                .setParameter("companyUserId", companyUserId)
                .getResultList();
    }

    // 마감된 공고 조회
    public List<JobPostingResponse.ListDTO> findClosedByCompanyUserId(Integer companyUserId) {
        String jpql = """
                    SELECT new com.example.leapit.jobposting.JobPostingResponse$ListDTO(
                        jpt.id,
                        jpt.title
                    )
                    FROM JobPosting jpt
                    JOIN jpt.user u
                    WHERE u.id = :companyUserId
                      AND jpt.deadline < CURRENT_DATE
                    ORDER BY jpt.createdAt DESC
                """;

        return em.createQuery(jpql, JobPostingResponse.ListDTO.class)
                .setParameter("companyUserId", companyUserId)
                .getResultList();
    }



    public List<JobPostingResponse.ItemDTO> findAllByFilter(
            Integer regionId,
            Integer subRegionId,
            CareerLevel career,
            String techStackCode,
            String positionLabel,
            SortType sortType,
            Integer sessionUserId
    ) {

        LocalDate today = LocalDate.now();

        StringBuilder jpql = new StringBuilder(
                "SELECT jp, jpts, ci " +
                        "FROM JobPosting jp " +
                        "LEFT JOIN JobPostingTechStack jpts ON jp.id = jpts.jobPosting.id " +
                        "LEFT JOIN CompanyInfo ci ON jp.user.id = ci.user.id " +
                        "WHERE jp.deadline >= :today"
        );

        // 지역 필터링
        if (regionId != null) {
            jpql.append(" AND jp.addressRegionId = :regionId");
        }

        // 서브지역 필터링
        if (subRegionId != null) {
            jpql.append(" AND jp.addressSubRegionId = :subRegionId");
        }

        // 경력 필터링
        if (career != null) {
            jpql.append(" AND :careerValue >= jp.minCareerLevel AND :careerValue <= jp.maxCareerLevel");
        }

        // 직무(포지션) 필터링
        if (positionLabel != null) {
            jpql.append(" AND jp.positionType = :positionLabel");
        }

        // 기술 스택 필터링
        if (techStackCode != null) {
            jpql.append(" AND EXISTS (" +
                    "SELECT 1 FROM JobPostingTechStack jpts2 " +
                    "WHERE jpts2.jobPosting.id = jp.id " +
                    "AND jpts2.techStack.code = :techStackCode" +
                    ")");
        }

        // 정렬
        if (sortType == SortType.POPULAR) {
            jpql.append(" ORDER BY jp.viewCount DESC");
        } else if (sortType == SortType.LATEST) {
            jpql.append(" ORDER BY jp.createdAt DESC");
        }

        Query query = em.createQuery(jpql.toString(), Object[].class);
        query.setParameter("today", today);

        if (regionId != null) query.setParameter("regionId", regionId);
        if (subRegionId != null) query.setParameter("subRegionId", subRegionId);
        if (career != null) query.setParameter("careerValue", career.value);
        if (techStackCode != null) query.setParameter("techStackCode", techStackCode);
        if (positionLabel != null) query.setParameter("positionLabel", positionLabel);

        List<Object[]> results = query.getResultList();
        List<JobPostingResponse.ItemDTO> dtos = new ArrayList<>();

        Integer lastJobPostingId = null;
        JobPostingResponse.ItemDTO currentDTO = null;

        for (Object[] result : results) {
            JobPosting jobPosting = (JobPosting) result[0];
            JobPostingTechStack techStack = (JobPostingTechStack) result[1];
            CompanyInfo companyInfo = (CompanyInfo) result[2];

            boolean isBookmarked = isBookmarked(jobPosting.getId(), sessionUserId);

            if (!jobPosting.getId().equals(lastJobPostingId)) {
                String address = companyInfo != null ? companyInfo.getAddress() : "주소 없음";
                String image = companyInfo != null ? companyInfo.getImage() : "이미지 없음";
                String companyName = companyInfo != null ? companyInfo.getCompanyName() : "회사명 없음";

                List<JobPostingTechStack> techStacks = new ArrayList<>();
                if (techStack != null) {
                    techStacks.add(techStack);
                }

                // JobPostingDTO 생성 시, isBookmarked 값 전달
                currentDTO = new JobPostingResponse.ItemDTO(
                        jobPosting, techStacks, address, image, companyName, isBookmarked
                );
                dtos.add(currentDTO);

                lastJobPostingId = jobPosting.getId();
            } else {
                if (techStack != null && currentDTO != null) {
                    currentDTO.getTechStacks().add(new CompanyInfoResponse.TechStackDTO(
                            techStack.getTechStack()
                    ));
                }
            }
        }
        return dtos;
    }

    // 북마크 확인 메서드
    public boolean isBookmarked(Integer jobPostingId, Integer sessionUserId) {
        JobPostingBookmark bookmark = em.createQuery(
                        "SELECT b FROM JobPostingBookmark b WHERE b.jobPosting.id = :jobPostingId AND b.user.id = :sessionUserId",
                        JobPostingBookmark.class)
                .setParameter("jobPostingId", jobPostingId)
                .setParameter("sessionUserId", sessionUserId)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
        return bookmark != null;
    }
}
