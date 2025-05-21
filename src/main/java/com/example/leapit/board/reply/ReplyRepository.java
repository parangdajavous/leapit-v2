package com.example.leapit.board.reply;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ReplyRepository {
    private final EntityManager em;

    // 특정 게시글의 댓글들 조회
    public List<Reply> findAllByBoardId(int boardId) {
        Query query = em.createQuery("select r from Reply r join fetch r.user where r.board.id = :boardId", Reply.class);
        query.setParameter("boardId", boardId);
        List<Reply> replies = query.getResultList();
        return replies;

    }

    // 특정 게시글의 댓글 삭제
    public void deleteByBoardId(Integer boardId) {
        em.createQuery("delete from Reply r where r.board.id = :boardId")
                .setParameter("boardId", boardId)
                .executeUpdate();
    }

    // 댓글 등록
    public Reply save(Reply reply) {
        em.persist(reply);
        return reply;
    }

    // 댓글 삭제
    public void deleteById(Integer id) {
        em.createQuery("delete from Reply r where r.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    // 댓글 조회
    public Optional<Reply> findById(Integer id) {
        return Optional.ofNullable(em.find(Reply.class, id));
    }
}
