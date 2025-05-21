package com.example.leapit.board.like;

import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;

    // 좋아요 등록
    @Transactional
    public LikeResponse.SaveDTO save(LikeRequest.SaveDTO reqDTO, Integer sessionUserId) {
        Like likePS = likeRepository.save(reqDTO.toEntity(sessionUserId));
        Long likeCount = likeRepository.findByBoardId(reqDTO.getBoardId());
        return new LikeResponse.SaveDTO(likePS.getId(), likeCount.intValue());
    }

    // 좋아요 삭제
    @Transactional
    public LikeResponse.DeleteDTO delete(Integer id, Integer sessionUserId) {
        Like likePS = likeRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        if (!likePS.getUser().getId().equals(sessionUserId)) throw new ExceptionApi403("권한이 없습니다.");

        Integer boardId = likePS.getBoard().getId();

        likeRepository.deleteById(id);

        Long likeCount = likeRepository.findByBoardId(boardId);

        return new LikeResponse.DeleteDTO(likeCount.intValue());
    }
}
