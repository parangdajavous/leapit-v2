package com.example.leapit.board;

import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit.board.like.Like;
import com.example.leapit.board.like.LikeRepository;
import com.example.leapit.board.reply.Reply;
import com.example.leapit.board.reply.ReplyRepository;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Transactional
    public BoardResponse.DTO save(BoardRequest.SaveDTO reqDTO, User sessionUser) {
        // 여기에서 영속 상태의 user로 변환
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("유저를 찾을 수 없습니다"));

        Board board = reqDTO.toEntity(userPS);
        Board boardPS = boardRepository.save(board);
        return new BoardResponse.DTO(boardPS);
    }

    @Transactional
    public BoardResponse.DTO update(BoardRequest.UpdateDTO reqDTO, Integer boardId, Integer sessionUserId) {
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        if (!boardPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }

        boardPS.update(reqDTO.getTitle(), reqDTO.getContent());

        return new BoardResponse.DTO(boardPS);
    }


    public BoardResponse.DTO getOne(Integer id, Integer sessionUserId) {
        Board boardPS = boardRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        if (!boardPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }
        return new BoardResponse.DTO(boardPS);
    }

    public List<BoardResponse.ListDTO> getList(Integer sessionUserId) {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(board -> new BoardResponse.ListDTO(board, sessionUserId))
                .toList();
    }

    public BoardResponse.DetailDTO getDetail(Integer id, Integer sessionUserId) {
        Board boardPS = boardRepository.findByIdJoinUserAndReplies(id)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다."));

        // 비로그인 상태 대비 기본값
        Boolean isLike = false;
        Integer likeId = null;

        // 로그인 상태면 좋아요 정보 조회
        if (sessionUserId != null) {
            Like like = likeRepository.findByUserIdAndBoardId(sessionUserId, id)
                    .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다."));
            ;
            if (like != null) {
                isLike = true;
                likeId = like.getId();
            }
        }

        // 좋아요 개수는 항상 조회 (로그인 여부 상관없이)
        Long likeCount = likeRepository.findByBoardId(id);

        List<Reply> replies = replyRepository.findAllByBoardId(id);


        BoardResponse.DetailDTO detailDTO = new BoardResponse.DetailDTO(boardPS, sessionUserId, isLike, likeCount.intValue(), likeId, replies);

        return detailDTO;
    }

    @Transactional
    public void delete(Integer id, Integer sessionUserId) {
        Board boardPS = boardRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        if (!boardPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }

        // 댓글 삭제
        replyRepository.deleteByBoardId(id);

        // 좋아요 삭제
        likeRepository.deleteByBoardId(id);

        boardRepository.deleteById(id);
    }
}
