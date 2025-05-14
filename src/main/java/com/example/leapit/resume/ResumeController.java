package com.example.leapit.resume;

import com.example.leapit._core.util.Resp;
import com.example.leapit.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ResumeController {
    private final ResumeService resumeService;
    private final HttpSession session;

    @GetMapping("/personal/resume") // TODO : /s/api 추가
    public ResponseEntity<?> getList(HttpServletRequest request) {
        //User sessionUser = (User) session.getAttribute("sessionUser");
        Integer userId = 1;
        ResumeResponse.ListDTO respDTO = resumeService.getList(userId); // TODO : (sessionUser.getId())
        return Resp.ok(respDTO);
    }
}
