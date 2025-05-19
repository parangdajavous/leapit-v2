package com.example.leapit.jobposting;

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

    // 채용공고 등록
    @PostMapping("/s/api/company/jobposting")
    public ResponseEntity<?> save(@RequestBody @Valid JobPostingRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        JobPostingResponse.CompanyDTO respDTO = jobPostingService.save(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }

    // 채용공고 등록 폼에 뿌릴 데이터 ex) 포지션, 지역, 커리어레벨 등
    @GetMapping("/s/api/company/jobposting/new")
    public ResponseEntity<?> getSaveForm() {
        JobPostingResponse.SaveDTO respDTO = jobPostingService.getSaveForm();
        return Resp.ok(respDTO);
    }

    // 기업 채용공고 상세보기
    @GetMapping("/s/api/company/jobposting/{id}/detail")
    public ResponseEntity<?> companyGetDetailForm(@PathVariable Integer id) {
        JobPostingResponse.CompanyDTO respDTO = jobPostingService.getDetailCompany(id);
        return Resp.ok(respDTO);
    }

    // 구직자 채용공고 상세보기
    @GetMapping("/s/api/personal/jobposting/{id}/detail")
    public ResponseEntity<?> personalGetDetailForm(@PathVariable Integer id) {
        JobPostingResponse.DetailPersonalDTO respDTO = jobPostingService.getDetailPersonal(id);
        return Resp.ok(respDTO);
    }
}