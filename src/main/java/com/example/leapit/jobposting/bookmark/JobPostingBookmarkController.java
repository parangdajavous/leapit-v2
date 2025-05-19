package com.example.leapit.jobposting.bookmark;

import com.example.leapit._core.util.Resp;
import com.example.leapit.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class JobPostingBookmarkController {
    private final JobPostingBookmarkService jobPostingBookmarkService;
    private final HttpSession session;

    // 개인 마이페이지 공고 스크랩 현황 관리
    @GetMapping("/s/personal/mypage/bookmark")
    public ResponseEntity<?> getMyBookmark() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        JobPostingBookmarkResponse.ViewDTO respDTO = jobPostingBookmarkService.getMyBookmark(sessionUser.getId());
        return Resp.ok(respDTO);
    }
}