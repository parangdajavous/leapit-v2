package com.example.leapit.reply;

import com.example.leapit.board.Board;
import com.example.leapit.board.BoardRepository;
import com.example.leapit.board.reply.Reply;
import com.example.leapit.board.reply.ReplyRepository;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

@Import({ReplyRepository.class, UserRepository.class, BoardRepository.class})
@DataJpaTest
public class ReplyRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void save_test() {
        // given
        Integer userId = 1;
        Integer boardId = 1;

        Optional<User> user = userRepository.findById(userId);
        Optional<Board> board = boardRepository.findById(boardId);

        Reply reply = Reply.builder()
                .user(user.get())
                .board(board.get())
                .content("Hello World")
                .build();

        // when
        Reply saveReply = replyRepository.save(reply);

        // eye
        System.out.println("=========댓글 저장 결과========");
        System.out.println("content: " + saveReply.getContent());
        System.out.println("name: " + saveReply.getUser().getName());
        System.out.println("boardId: " + saveReply.getBoard().getId());
        System.out.println("===================");
    }

    @Test
    public void findById_test() {
        // given
        Integer id = 1;

        // when
        Optional<Reply> reply = replyRepository.findById(id);

        // eye
        if (reply.isPresent()) {
            System.out.println("=========댓글 조회 결과========");
            System.out.println("content: " + reply.get().getContent());
            System.out.println("name: " + reply.get().getUser().getName());
            System.out.println("boardId: " + reply.get().getBoard().getId());
            System.out.println("===================");
        } else {
            System.out.println("해당 ID의 댓글이 없습니다.");
        }

    }

    @Test
    public void deleteById_test() {
        // given
        Integer id = 1;

        // when
        replyRepository.deleteById(id);

        // eye
        Optional<Reply> reply = replyRepository.findById(id);
        if (reply.isEmpty()) {
            System.out.println("댓글 삭제됨");
        } else {
            System.out.println("댓글 남아있음: " + reply);
        }

    }

    @Test
    public void deleteByBoardId_test() {
        // given
        Integer boardId = 1;

        // when
        replyRepository.deleteByBoardId(boardId);

        // eye
        Optional<Reply> reply = replyRepository.findById(boardId);
        if (reply.isEmpty()) {
            System.out.println("댓글 삭제됨");
        } else {
            System.out.println("댓글 남아있음: " + reply);
        }
    }

    @Test
    public void findAllByBoardId_test() {
        // given
        Integer boardId = 1;

        // when
        List<Reply> replyList = replyRepository.findAllByBoardId(boardId);

        // eye
        System.out.println("========== 댓글 목록 ==========");
        for (Reply reply : replyList) {
            System.out.println("ID: " + reply.getId());
            System.out.println("내용: " + reply.getContent());
            System.out.println("작성자: " + reply.getUser().getName());
            System.out.println("-------------------------------");
        }


    }
}