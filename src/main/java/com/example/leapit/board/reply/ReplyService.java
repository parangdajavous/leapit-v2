package com.example.leapit.board.reply;

import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit.board.Board;
import com.example.leapit.board.BoardRepository;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 등록
    @Transactional
    public ReplyResponse.DTO save(ReplyRequest.SaveDTO reqDTO, User sessionUser) {
        // 여기에서 영속 상태의 user로 변환
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("유저를 찾을 수 없습니다"));

        Board boardPS = boardRepository.findById(reqDTO.getBoardId())
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        Reply replyPS = replyRepository.save(reqDTO.toEntity(userPS,boardPS));
        return new ReplyResponse.DTO(replyPS);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Integer id, Integer sessionUserId) {
        Reply replyPS = replyRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        if (!replyPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }

        replyRepository.deleteById(id);
    }
}
