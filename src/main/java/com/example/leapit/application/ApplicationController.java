package com.example.leapit.application;

import com.example.leapit._core.error.ex.ExceptionApi401;
import com.example.leapit._core.util.Resp;
import com.example.leapit.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ApplicationController {
    private final ApplicationService applicationService;
    private final HttpSession session;

    // 개인 마이페이지 지원 현황 관리
    @GetMapping("/s/api/personal/mypage/application")
    public ResponseEntity<?> getMyApplication() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ApplicationResponse.MyPageDTO respDTO = applicationService.getMyApplication(sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 기업 지원자 현황 관리
    @GetMapping("/s/api/company/applicant")
    public ResponseEntity<?> getApplicant(ApplicationRequest.ApplicantListDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        ApplicationResponse.ApplicantPageDTO respDTO =
                applicationService.getApplicant(sessionUser.getId(), reqDTO);

        return Resp.ok(respDTO);
    }

    // 지원서 합격,불합격 처리
    @PutMapping("/s/api/company/applicant/{id}/pass")
    public ResponseEntity<?> updatePass(@PathVariable("id") Integer id, @RequestBody ApplicationRequest.UpdatePassDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        ApplicationResponse.UpdatePassDTO respDTO = applicationService.updatePass(id, reqDTO, sessionUser.getId());
        return Resp.ok(respDTO);
    }
}
