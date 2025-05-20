package com.example.leapit.board.like;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class LikeRepository {
    private final EntityManager em;

    public Optional<Like> findByUserIdAndBoardId(Integer userId, Integer boardId) {
        Query query = em.createQuery("select li from Like li where li.user.id = :userId and li.board.id = :boardId", Like.class);
        query.setParameter("userId", userId);
        query.setParameter("boardId", boardId);
        try {
            Like likePS = (Like) query.getSingleResult();
            return Optional.of(likePS);
        } catch (Exception e) {
            return Optional.ofNullable(null);
        }
    }

    public Long findByBoardId(int boardId) {
        Query query = em.createQuery("select count(li) from Like li where li.board.id = :boardId");
        query.setParameter("boardId", boardId);
        Long count = (Long) query.getSingleResult();
        return count;
    }

    public void deleteByBoardId(Integer boardId) {
        Query query = em.createQuery("delete from Like li where li.board.id = :boardId");
        query.setParameter("boardId", boardId);
        query.executeUpdate();
    }

    public Like save(Like like) {
        em.persist(like);
        return like;
    }

    public void deleteById(Integer id) {
        Query query = em.createQuery("delete from Like li where li.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public Optional<Like> findById(Integer id) {
        return Optional.ofNullable(em.find(Like.class, id));
    }
}
