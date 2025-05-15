package com.example.leapit.companyinfo;

import com.example.leapit._core.util.Resp;
import com.example.leapit.common.enums.Role;
import com.example.leapit.user.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CompanyInfoController {
    private final CompanyInfoService companyInfoService;
    private final HttpSession session;
    // 기업 정보 삭제 기능 X -> 회원 탈퇴로 처리한다고 가정한다.

    @PostMapping("/s/api/company/info")
    public ResponseEntity<?> save(@Valid @RequestBody CompanyInfoRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        CompanyInfoResponse.DTO respDTO = companyInfoService.save(reqDTO, sessionUser);

        return Resp.ok(respDTO);
    }
}
