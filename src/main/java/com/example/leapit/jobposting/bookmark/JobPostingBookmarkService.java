package com.example.leapit.jobposting.bookmark;

import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit.application.ApplicationRepository;
import com.example.leapit.application.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JobPostingBookmarkService {
    private final JobPostingBookmarkRepository jobPostingBookmarkRepository;
    private final ApplicationRepository applicationRepository;

    // 개인 마이페이지 공고 스크랩 현황 관리
    public JobPostingBookmarkResponse.ViewDTO getMyBookmark(Integer userId) {
        if (userId == null) throw new ExceptionApi404("회원정보가 존재하지 않습니다.");

        // 지원 현황 통계
        ApplicationResponse.StatusDTO statusDTO = applicationRepository.findStatusByUserId(userId);

        // 스크랩한 공고 목록 조회
        List<JobPostingBookmarkResponse.ItemDTO> bookmarkListDTO = jobPostingBookmarkRepository.findItemsByuserId(userId);

        // respDTO에 담기
        JobPostingBookmarkResponse.ViewDTO respDTO = new JobPostingBookmarkResponse.ViewDTO(bookmarkListDTO, statusDTO);

        return respDTO;
    }
}
