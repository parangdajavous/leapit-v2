package com.example.leapit.jobposting.bookmark;

import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit.application.ApplicationRepository;
import com.example.leapit.application.ApplicationResponse;
import com.example.leapit.jobposting.JobPosting;
import com.example.leapit.jobposting.JobPostingRepository;
import com.example.leapit.jobposting.JobPostingService;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JobPostingBookmarkService {
    private final JobPostingBookmarkRepository jobPostingBookmarkRepository;
    private final ApplicationRepository applicationRepository;
    private final JobPostingRepository jobPostingRepository;

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

    @Transactional
    public JobPostingBookmarkResponse.DTO save(JobPostingBookmarkRequest.SaveDTO reqDTO, User sessionUser) {
        JobPosting jobPosting = jobPostingRepository.findById(reqDTO.getJobPostingId())
                .orElseThrow(()-> new ExceptionApi404("지원 정보가 존재하지 않습니다."));
        if (jobPosting == null) {
            throw new ExceptionApi404("지원 정보가 존재하지 않습니다.");
        }

        JobPostingBookmark bookmark = JobPostingBookmark.builder()
                .user(sessionUser)
                .jobPosting(jobPosting)
                .build();

        JobPostingBookmark jobPostingBookmarkPS = jobPostingBookmarkRepository.save(bookmark);

        return new JobPostingBookmarkResponse.DTO(jobPostingBookmarkPS);
    }

    @Transactional
    public void delete(Integer bookmarkId, Integer sessionUserId) {

        // 북마크 조회
        JobPostingBookmark bookmark = jobPostingBookmarkRepository.findById(bookmarkId)
                .orElseThrow(()->new ExceptionApi404("해당 스크랩이 존재하지 않습니다."));

        // 권한 확인
        if (!bookmark.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다.");
        }

        // 북마크 삭제
        jobPostingBookmarkRepository.delete(bookmark);
    }
}
