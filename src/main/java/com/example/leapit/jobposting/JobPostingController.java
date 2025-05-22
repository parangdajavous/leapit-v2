package com.example.leapit.jobposting;

import com.example.leapit._core.util.JwtUtil;
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
public class JobPostingController {
    private final JobPostingService jobPostingService;
    private final HttpSession session;

    // 채용공고 등록 -> 통합 테스트 코드 작성 완료
    @PostMapping("/s/api/company/jobposting")
    public ResponseEntity<?> save(@RequestBody @Valid JobPostingRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        JobPostingResponse.DTO respDTO = jobPostingService.save(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }

    // 채용공고 등록 폼에 뿌릴 데이터 ex) 포지션, 지역, 커리어레벨 등 -> 통합 테스트 코드 작성 완료
    @GetMapping("/s/api/company/jobposting/new")
    public ResponseEntity<?> getSaveForm() {
        JobPostingResponse.SaveDTO respDTO = jobPostingService.getSaveForm();
        return Resp.ok(respDTO);
    }

    // 기업 채용공고 상세보기 -> 통합 테스트 코드 작성 완료
    @GetMapping("/s/api/company/jobposting/{id}/detail")
    public ResponseEntity<?> companyGetDetailForm(@PathVariable("id") Integer id) {
        JobPostingResponse.DTO respDTO = jobPostingService.getDetailCompany(id);
        return Resp.ok(respDTO);
    }

    // 구직자 채용공고 상세보기 -> 통합 테스트 코드 작성 완료
    @GetMapping("/s/api/personal/jobposting/{id}/detail")
    public ResponseEntity<?> personalGetDetailForm(@PathVariable("id") Integer id) {
        JobPostingResponse.DetailPersonalDTO respDTO = jobPostingService.getDetailPersonal(id);
        return Resp.ok(respDTO);
    }

    // 채용공고 삭제 -> 통합 테스트 코드 작성 완료
    @DeleteMapping("/s/api/company/jobposting/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        jobPostingService.delete(id, sessionUser.getId());
        return Resp.ok(null);
    }

    // 구직자 - 채용공고 목록(공고현황 페이지(필터))
    // /api/jobposting -> get_list_test
    // /s/api/personal/jobposting -> get_personal_list_test
    @GetMapping({"/api/jobposting", "/s/api/personal/jobposting"})
    public ResponseEntity<?> getPersonalList(JobPostingRequest.FilterDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Integer sessionUserId = (sessionUser != null) ? sessionUser.getId() : null;

        JobPostingResponse.FilteredListDTO respDTO =
                jobPostingService.getPersonalList(reqDTO,
                        sessionUserId
                );
        return Resp.ok(respDTO);
    }

    // 채용공고 수정 화면 -> 통합 테스트 코드 작성 완료
    @GetMapping("/s/api/company/jobposting/{id}/edit")
    public ResponseEntity<?> getUpdateForm(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        JobPostingResponse.UpdateDTO respDTO = jobPostingService.getUpdateForm(id, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 채용공고 수정 -> 통합 테스트 코드 작성 완료
    @PutMapping("/s/api/company/jobposting/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @Valid @RequestBody JobPostingRequest.UpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        JobPostingResponse.DTO respDTO = jobPostingService.update(id, sessionUser.getId(), reqDTO);
        return Resp.ok(respDTO);
    }

    // 기업 - 채용공고 목록 -> 통합 테스트 코드 작성 완료
    @GetMapping("/s/api/company/jobposting")
    public ResponseEntity<?> getCompanyList() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        JobPostingResponse.ListByStatusDTO respDTO = jobPostingService.getCompanyList(sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 메인화면
    @GetMapping("/")
    public ResponseEntity<?> index(
            @RequestHeader(value = "Authorization", required = false) String accessToken) {

        Integer userId = null;

        // 1. 세션 기반 인증
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser != null) {
            userId = sessionUser.getId();
        }

        // 2. 토큰 기반 인증 (세션이 없을 때만)
        else if (accessToken != null && accessToken.startsWith("Bearer ")) {
            try {
                String token = accessToken.replace("Bearer ", "");
                userId = JwtUtil.getUserId(token);
            } catch (Exception e) {
                System.out.println("JWT 파싱 실패: " + e.getMessage());
                // userId는 null로 둬도 됨 → 북마크 없이 동작
            }
        }
        JobPostingResponse.MainDTO respDTO = jobPostingService.index(userId);
        return Resp.ok(respDTO);
    }
}