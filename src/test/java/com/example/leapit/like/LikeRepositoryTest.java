package com.example.leapit.like;

import com.example.leapit.board.Board;
import com.example.leapit.board.BoardRepository;
import com.example.leapit.board.like.Like;
import com.example.leapit.board.like.LikeRepository;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@Import({LikeRepository.class, BoardRepository.class, UserRepository.class})
@DataJpaTest
public class LikeRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private LikeRepository likeRepository;

    @Test
    public void save_test() {
        // given
        Integer userId = 1;
        Integer boardId = 3;

        Optional<User> userPs = userRepository.findById(userId);
        Optional<Board> boardPS = boardRepository.findById(boardId);

        Like like = Like.builder()
                .user(userPs.get())
                .board(boardPS.get())
                .build();

        // when
        Like saveLike = likeRepository.save(like);
        Long likeCount = likeRepository.findByBoardId(boardId);

        // eye
        System.out.println("================좋아요 저장결과================");
        System.out.println("boardId: " + saveLike.getBoard().getId());
        System.out.println("userId: " + saveLike.getUser().getId());
        System.out.println("likeCount: " + likeCount.intValue());
        System.out.println("---------------------------------------------");


    }

    @Test
    public void findByUserIdAndBoardId_test() {
        // given
        Integer userId = 1;
        Integer boardId = 1;

        // when
        Optional<Like> likeOP = likeRepository.findByUserIdAndBoardId(userId, boardId);

        // eye
        if (likeOP.isPresent()) {
            System.out.println("Like exists: ID = " + likeOP.get().getId());
        } else {
            System.out.println("Like not found.");
        }

    }

    @Test
    public void findByBoardId_test() {
        // given
        Integer boardId = 1;

        // when
        Long count = likeRepository.findByBoardId(boardId);

        // eye
        System.out.println("Like Count for boardId = " + count.intValue());
    }

    @Test
    public void deleteByBoardId_test() {
        // given
        Integer boardId = 2;

        // given
        likeRepository.deleteByBoardId(boardId);

        // eye
        Long count = likeRepository.findByBoardId(boardId);
        System.out.println("삭제 후 Like Count = " + count.intValue()); // 0 기대

    }

    @Test
    public void deleteById_test() {
        // given
        Integer boardId = 1;
        Integer id = 1;

        // when
        likeRepository.deleteById(boardId);

        // eye
        Optional<Like> likeOP = likeRepository.findById(id);
        System.out.println("삭제 후 존재 여부: " + likeOP.isPresent()); // false 기대

    }

    @Test
    public void findById_test() {
        // given
        Integer id = 1;

        // when
        Optional<Like> likePS = likeRepository.findById(id);

        // eye
        if (likePS.isPresent()) {
            System.out.println("Like ID: " + likePS.get().getId());
        } else {
            System.out.println("좋아요를 찾을 수 없음");
        }

    }

}
