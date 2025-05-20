package com.example.leapit.application;

import com.example.leapit._core.util.Resp;
import com.example.leapit.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
