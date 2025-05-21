package com.example.leapit.application.bookmark;

import com.example.leapit._core.error.ex.ExceptionApi401;
import com.example.leapit._core.util.Resp;
import com.example.leapit.user.User;
import com.example.leapit.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ApplicationBookmarkController {
    private final ApplicationBookmarkService bookmarkService;
    private final HttpSession session;

    // 기업 스크랩 등록 application_bookmark
    @PostMapping("/s/api/company/applicationbookmark")
    public ResponseEntity<?> save(@Valid @RequestBody ApplicationBookmarkRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ApplicationBookmarkResponse.SaveDTO respDTO = bookmarkService.save(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }

    // 기업 스크랩 삭제 application_bookmark
    @DeleteMapping("/s/api/company/applicationbookmark/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer bookmarkId) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        bookmarkService.delete(bookmarkId, sessionUser.getId());
        return Resp.ok(null);
    }
}
