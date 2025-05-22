package com.example.leapit.resume;

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
public class ResumeController {
    private final ResumeService resumeService;
    private final HttpSession session;

    // 이력서 목록
    @GetMapping("/s/api/personal/resume")
    public ResponseEntity<?> getList() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ResumeResponse.ListDTO respDTO = resumeService.getList(sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 이력서 삭제
    @DeleteMapping("/s/api/personal/resume/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        resumeService.delete(id, sessionUser.getId());
        return Resp.ok(null);
    }

    // 이력서 등록 화면
    @GetMapping("/s/api/personal/resume/new")
    public ResponseEntity<?> getSaveForm(){
        User sessionUser = (User) session.getAttribute("sessionUser");
        ResumeResponse.SaveDTO respDTO = resumeService.getSaveForm(sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 이력서 등록
    @PostMapping("/s/api/personal/resume")
    public ResponseEntity<?> save(@Valid @RequestBody ResumeRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ResumeResponse.DTO respDTO = resumeService.save(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }

    // 이력서 수정 화면
    @GetMapping("/s/api/personal/resume/{id}/edit")
    public ResponseEntity<?> getUpdateForm(@PathVariable("id") int id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ResumeResponse.UpdateDTO respDTO = resumeService.getUpdateForm(id, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 이력서 수정
    @PutMapping("/s/api/personal/resume/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @Valid @RequestBody ResumeRequest.UpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ResumeResponse.DTO respDTO = resumeService.update(id, reqDTO, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 이력서 상세보기
    @GetMapping("/s/api/personal/resume/{id}/detail")
    public ResponseEntity<?> detail(@PathVariable("id") int id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ResumeResponse.DetailDTO respDTO = resumeService.getDetail(id, sessionUser, null);
        return Resp.ok(respDTO);
    }
}
