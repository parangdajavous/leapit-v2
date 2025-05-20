package com.example.leapit.jobposting.bookmark;
import com.example.leapit.resume.Resume;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JobPostingBookmarkRepository {
    private final EntityManager em;

    // 북마크 등록
    public JobPostingBookmark save(JobPostingBookmark bookmark) {
        em.persist(bookmark);
        return bookmark;
    }

    //  개인 마이페이지 공고 스크랩 목록 조회
    public List<JobPostingBookmarkResponse.ItemDTO> findItemsByuserId(Integer userId) {
        String jpql = """
        SELECT jp.id, ci.companyName, jp.title, jp.deadline
        FROM JobPostingBookmark jb
        JOIN jb.jobPosting jp
        JOIN jp.user u
        JOIN CompanyInfo ci ON ci.user = u
        WHERE jb.user.id = :userId
        ORDER BY jp.deadline DESC
    """;

        List<Object[]> resultList = em.createQuery(jpql, Object[].class)
                .setParameter("userId", userId)
                .getResultList();

        List<JobPostingBookmarkResponse.ItemDTO> dtos = new ArrayList<>();

        for (Object[] row : resultList) {
            Integer jobPostingId = (Integer) row[0];
            String companyName = (String) row[1];
            String title = (String) row[2];
            LocalDate deadline = (LocalDate) row[3];

            dtos.add(new JobPostingBookmarkResponse.ItemDTO(
                    jobPostingId, companyName, title, deadline
            ));
        }

        return dtos;
    }

    public Optional<JobPostingBookmark> findById(Integer id) {
        return Optional.ofNullable(em.find(JobPostingBookmark.class, id));
    }

    public void delete(JobPostingBookmark bookmark) {
        em.remove(bookmark);
    }
}
