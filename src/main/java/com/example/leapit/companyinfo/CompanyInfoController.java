package com.example.leapit.companyinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CompanyInfoController {
    private final CompanyInfoService companyInfoService;
    // 기업 정보 삭제 기능 X -> 회원 탈퇴로 처리한다고 가정한다.
}
