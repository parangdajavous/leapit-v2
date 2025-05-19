package com.example.leapit.resume;

import com.example.leapit._core.util.Resp;
import com.example.leapit.user.User;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/s/api/personal/resume")
    public ResponseEntity<?> getList() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ResumeResponse.ListDTO respDTO = resumeService.getList(sessionUser.getId());
        return Resp.ok(respDTO);
    }

    @DeleteMapping("/s/api/personal/resume/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        resumeService.delete(id, sessionUser.getId());
        return Resp.ok(null);
    }

    @GetMapping("/s/api/personal/resume/new")
    public ResponseEntity<?> getSaveForm(){
        User sessionUser = (User) session.getAttribute("sessionUser");
        ResumeResponse.SaveDTO respDTO = resumeService.getSaveForm(sessionUser);
        return Resp.ok(respDTO);
    }

    @PostMapping("/s/api/personal/resume")
    public ResponseEntity<?> save(@Valid @RequestBody ResumeRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ResumeResponse.DTO respDTO = resumeService.save(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }
}
