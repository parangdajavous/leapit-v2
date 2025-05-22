package com.example.leapit.jobposting.bookmark;

import com.example.leapit._core.error.ex.ExceptionApi401;
import com.example.leapit._core.util.Resp;
import com.example.leapit.user.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class JobPostingBookmarkController {
    private final JobPostingBookmarkService jobPostingBookmarkService;
    private final HttpSession session;

    // 개인 마이페이지 공고 스크랩 현황 관리
    @GetMapping("/s/api/personal/mypage/bookmark")
    public ResponseEntity<?> getMyBookmark() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        JobPostingBookmarkResponse.ViewDTO respDTO = jobPostingBookmarkService.getMyBookmark(sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 개인 스크랩 등록
    @PostMapping("/s/api/personal/jobpostingbookmark")
    public ResponseEntity<?> save(@Valid @RequestBody JobPostingBookmarkRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        JobPostingBookmarkResponse.DTO respDTO = jobPostingBookmarkService.save(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }

    // 개인 스크랩 삭제 job_posting_bookmark
    @DeleteMapping("/s/api/personal/jobpostingbookmark/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer bookmarkId) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        jobPostingBookmarkService.delete(bookmarkId, sessionUser.getId());
        return Resp.ok(null);
    }
}