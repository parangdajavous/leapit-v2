package com.example.leapit.companyinfo;

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
public class CompanyInfoController {
    private final CompanyInfoService companyInfoService;
    private final HttpSession session;
    // 기업 정보 삭제 기능 X -> 회원 탈퇴로 처리한다고 가정한다.

    // 기업정보 등록
    @PostMapping("/s/api/company/info")
    public ResponseEntity<?> save(@Valid @RequestBody CompanyInfoRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        CompanyInfoResponse.DTO respDTO = companyInfoService.save(reqDTO, sessionUser);

        return Resp.ok(respDTO);
    }

    // 기업정보 수정
    @PutMapping("/s/api/company/info/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @Valid @RequestBody CompanyInfoRequest.UpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        CompanyInfoResponse.DTO respDTO = companyInfoService.update(id, sessionUser.getId(), reqDTO);

        return Resp.ok(respDTO);
    }

    // 기업정보 보기
    @GetMapping("/s/api/company/companyinfo/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        CompanyInfoResponse.DTO respDTO = companyInfoService.getOne(id, sessionUser.getId());

        return Resp.ok(respDTO);
    }

    // 기업정보 상세보기
    @GetMapping("/s/api/company/companyinfo/{id}/detail")
    public ResponseEntity<?> getDetailCompany(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        CompanyInfoResponse.DetailDTO respDTO = companyInfoService.getDetail(id, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 구직자 - 기업정보 상세보기
    @GetMapping("/api/personal/companyinfo/{id}/detail")
    public ResponseEntity<?> getDetailPersonal(@PathVariable("id") Integer id) {
        CompanyInfo companyInfo = companyInfoService.findById(id);
        Integer companyUserId = companyInfo.getUser().getId();

        CompanyInfoResponse.DetailDTO respDTO = companyInfoService.getDetail(id, companyUserId);
        return Resp.ok(respDTO);
    }
}
