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


    // 주어진 사용자 ID와 게시글 ID를 기반으로 해당 사용자의 좋아요 정보를 조회
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

    // 특정 게시글의 좋아요 수 count
    public Long findByBoardId(int boardId) {
        Query query = em.createQuery("select count(li) from Like li where li.board.id = :boardId");
        query.setParameter("boardId", boardId);
        Long count = (Long) query.getSingleResult();
        return count;
    }

    // 특정 게시글의 좋아요 삭제
    public void deleteByBoardId(Integer boardId) {
        Query query = em.createQuery("delete from Like li where li.board.id = :boardId");
        query.setParameter("boardId", boardId);
        query.executeUpdate();
    }

    // 좋아요 등록
    public Like save(Like like) {
        em.persist(like);
        return like;
    }

    // 좋아요 삭제
    public void deleteById(Integer id) {
        Query query = em.createQuery("delete from Like li where li.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    // 좋아요 조회
    public Optional<Like> findById(Integer id) {
        return Optional.ofNullable(em.find(Like.class, id));
    }
}
