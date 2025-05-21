package com.example.leapit.application.bookmark;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ApplicationBookmarkRepository {
    private final EntityManager em;

    public Optional<ApplicationBookmark> findById(Integer applicationBookmarkId) {
        return Optional.ofNullable(em.find(ApplicationBookmark.class, applicationBookmarkId));
    }

    public Optional<ApplicationBookmark> findByUserIdAndApplicationId(Integer userId, Integer applicationId) {
        try {
            ApplicationBookmark result = em.createQuery("""
                SELECT ab FROM ApplicationBookmark ab
                WHERE ab.user.id = :userId AND ab.application.id = :applicationId
            """, ApplicationBookmark.class)
                    .setParameter("userId", userId)
                    .setParameter("applicationId", applicationId)
                    .getSingleResult();

            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public ApplicationBookmark save(ApplicationBookmark bookmark) {
        em.persist(bookmark);
        return bookmark;
    }

    public void delete(ApplicationBookmark bookmark) {
        em.remove(bookmark);
    }
}
