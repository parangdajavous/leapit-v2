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

    public Board save(Board board) {
        em.persist(board);
        return board;
    }

    public Optional<Board> findById(Integer id) {
        Board boardPS = em.find(Board.class, id);
        return Optional.ofNullable(boardPS);
    }

    public List<Board> findAll() {
        Query query = em.createQuery("select b from Board b join fetch b.user order by b.id desc", Board.class);
        return query.getResultList();
    }

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

    public void deleteById(Integer id) {
        em.createQuery("delete from Board b where b.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}

