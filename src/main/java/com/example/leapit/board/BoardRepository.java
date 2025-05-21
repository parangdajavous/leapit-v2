package com.example.leapit.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    // 게시글 등록
    public Board save(Board board) {
        em.persist(board);
        return board;
    }

    // 게시글 조회
    public Optional<Board> findById(Integer id) {
        Board boardPS = em.find(Board.class, id);
        return Optional.ofNullable(boardPS);
    }

    // 게시글 전체 조회
    public List<Board> findAll() {
        Query query = em.createQuery("select b from Board b join fetch b.user order by b.id desc", Board.class);
        return query.getResultList();
    }

    // 게시글 ID를 기준으로 게시글, 작성자, 댓글 목록, 댓글 작성자 조회
    public Optional<Board> findByIdJoinUserAndReplies(Integer id) {
        try {
            Query query = em.createQuery("select b from Board b join fetch b.user left join fetch b.replies r left join fetch r.user where b.id = :id order by r.id desc", Board.class);
            query.setParameter("id", id);
            Board boardPS = (Board) query.getSingleResult();
            return Optional.of(boardPS);
        } catch (Exception e) {
            return Optional.ofNullable(null);
        }

    }

    // 게시글 삭제
    public void deleteById(Integer id) {
        em.createQuery("delete from Board b where b.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}

