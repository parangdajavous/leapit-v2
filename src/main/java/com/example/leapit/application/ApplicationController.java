package com.example.leapit.application;

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

    // 특정 채용공고에 대한 이력서 지원하기 화면
    @GetMapping("/s/api/personal/jobposting/{id}/apply")
    public ResponseEntity<?> getApplyForm(@PathVariable Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ApplicationResponse.ApplyDTO respDTO = applicationService.getApplyForm(id, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 채용공고에 이력서를 지원
    @PostMapping("/s/api/personal/application")
    public ResponseEntity<?> save(@Valid @RequestBody ApplicationRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ApplicationResponse.SaveDTO respDTO = applicationService.save(reqDTO, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 기업 지원 스크랩 application_bookmark
    @PutMapping("/s/api/company/application/{id}/bookmark")
    public ResponseEntity<?> updateBookmark(@PathVariable("id") Integer applicationId) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ApplicationResponse.DTO respDTO = applicationService.updateBookmark(applicationId, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 기업 지원서 상세보기
    @GetMapping("/s/api/company/applicant/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ApplicationResponse.DetailDTO respDTO = applicationService.getDetail(id, sessionUser);
        return Resp.ok(respDTO);
    }
}
