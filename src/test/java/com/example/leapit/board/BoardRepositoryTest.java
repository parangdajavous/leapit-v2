package com.example.leapit.board;

import com.example.leapit.board.like.LikeRepository;
import com.example.leapit.board.reply.Reply;
import com.example.leapit.board.reply.ReplyRepository;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@Import({BoardRepository.class, UserRepository.class, ReplyRepository.class, LikeRepository.class})
public class BoardRepositoryTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private LikeRepository likeRepository;


    @Test
    public void save_test() {
        // given
        User userPS = userRepository.findById(1)
                .orElseThrow();

        String title = "제목";
        String content = "내용";
        Board board = Board.builder()
                .title(title)
                .content(content)
                .user(userPS)
                .build();

        // when
        Board savedBoard = boardRepository.save(board);

        // eye
        System.out.println(savedBoard.getTitle());
        System.out.println(savedBoard.getContent());
        System.out.println(savedBoard.getUser().getName());
    }

    @Test
    public void findAll_test() {

        // when
        List<Board> board = boardRepository.findAll();

        // eye
        for (Board b : board) {
            System.out.println("====================");
            System.out.println("id: " + b.getId());
            System.out.println("title: " + b.getTitle());
            System.out.println("content: " + b.getContent());
            System.out.println("user: " + b.getUser().getName());
            System.out.println("createdAt: " + b.getCreatedAt());


        }

    }

    @Test
    public void findById_test() {
        // given
        Integer id = 1;

        // when
        Optional<Board> boardOP = boardRepository.findById(id);

        // eye
        if (boardOP.isPresent()) {
            Board board = boardOP.get(); // 실제 Board 꺼내기
            System.out.println("====== 게시글 정보 ======");
            System.out.println("id: " + board.getId());
            System.out.println("title: " + board.getTitle());
            System.out.println("content: " + board.getContent());
            System.out.println("user: " + board.getUser().getName());
            System.out.println("createdAt: " + board.getCreatedAt());

        } else {
            System.out.println("해당 ID의 게시글이 없습니다.");
        }

    }

    @Test
    public void findByIdJoinUserAndReplies_test() {
        // given
        Integer id = 1;

        // when
        Optional<Board> boardOP = boardRepository.findByIdJoinUserAndReplies(id);

        // eye
        if (boardOP.isPresent()) {
            Board board = boardOP.get();
            System.out.println("===================");
            System.out.println("id: " + board.getId());
            System.out.println("title: " + board.getTitle());
            System.out.println("content: " + board.getContent());
            System.out.println("user: " + board.getUser().getName());
            System.out.println("createdAt: " + board.getCreatedAt());
            System.out.println("===================");
            System.out.println("댓글 목록:");
            for (Reply reply : board.getReplies()) {
                System.out.println("----------");
                System.out.println("댓글 ID: " + reply.getId());
                System.out.println("댓글 user: " + reply.getUser().getName());
                System.out.println("내용: " + reply.getContent());
            }
        } else {
            System.out.println("해당 ID의 게시글이 없습니다.");
        }
    }

    @Test
    public void deleteById_test() {
        // given
        Integer id = 1;

        // when
        likeRepository.deleteByBoardId(id);  // 좋아요 삭제
        replyRepository.deleteByBoardId(id); // 댓글 삭제
        boardRepository.deleteById(id);  // 게시글 삭제

        // eye
        Long likeCount = likeRepository.findByBoardId(id);
        if (likeCount == 0) {
            System.out.println("좋아요 삭제됨");
        } else {
            System.out.println("좋아요 남아있음: " + likeCount + "개");
        }

        List<Reply> replies = replyRepository.findAllByBoardId(id);
        if (replies.isEmpty()) {
            System.out.println("댓글 삭제됨");
        } else {
            System.out.println("댓글 남아있음: " + replies);
        }

        Optional<Board> boardOP = boardRepository.findById(id);
        if (boardOP.isEmpty()) {
            System.out.println("게시글 삭제됨");
        } else {
            System.out.println("게시글 아직 존재함: " + boardOP.get());
        }

    }
}
