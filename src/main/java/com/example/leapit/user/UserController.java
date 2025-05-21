package com.example.leapit.user;

import com.example.leapit._core.error.ex.ExceptionApi400;
import com.example.leapit._core.util.JwtUtil;
import com.example.leapit._core.util.Resp;
import com.example.leapit.common.enums.Role;
import com.example.leapit.jobposting.JobPostingResponse;
import com.example.leapit.jobposting.JobPostingService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final JobPostingService jobPostingService;
    private final HttpSession session;

    // 개인 회원가입
    @PostMapping("/personal/join")
    public ResponseEntity<?> personalJoin(@Valid @RequestBody UserRequest.PersonalJoinDTO reqDTO, Errors errors) {
        if (reqDTO.getRole()!= Role.PERSONAL) throw new ExceptionApi400("잘못된 요청입니다");
        UserResponse.DTO respDTO = userService.join(reqDTO);
        return Resp.ok(respDTO);
    }

    // 기업 회원가입
    @PostMapping("/company/join")
    public ResponseEntity<?> companyJoin(@Valid @RequestBody UserRequest.CompanyJoinDTO reqDTO) {
        if (reqDTO.getRole()!= Role.COMPANY) throw new ExceptionApi400("잘못된 요청입니다");
        UserResponse.DTO respDTO = userService.join(reqDTO);
        return Resp.ok(respDTO);
    }

    // 유저네임 중복체크
    @GetMapping("/api/check-username-available/{username}")
    public ResponseEntity<?> checkUsernameAvailable(@PathVariable("username") String username) {
        Map<String, Object> respDTO = userService.checkUsernameAvailable(username);
        return Resp.ok(respDTO);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest.LoginDTO loginDTO, Errors errors) {
        UserResponse.TokenDTO respDTO = userService.login(loginDTO);
        return Resp.ok(respDTO);
    }

    // 기업 유저 정보 수정
    @PutMapping("/s/company/user")
    public ResponseEntity<?> companyUpdate(@Valid @RequestBody UserRequest.CompanyUpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (!reqDTO.getNewPassword().equals(reqDTO.getConfirmPassword())) throw new ExceptionApi400("입력한 비밀번호가 다릅니다.");
        UserResponse.UpdateDTO respDTO = userService.update(reqDTO, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 개인 유저 정보 수정
    @PutMapping("/s/personal/user")
    public ResponseEntity<?> personalUpdate(@Valid @RequestBody UserRequest.PersonalUpdateDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (!reqDTO.getNewPassword().equals(reqDTO.getConfirmPassword())) throw new ExceptionApi400("입력한 비밀번호가 다릅니다.");
        UserResponse.UpdateDTO respDTO = userService.update(reqDTO, sessionUser.getId());
        return Resp.ok(respDTO);
    }



    // 개인 유저 정보 수정 화면
    @GetMapping("/s/api/personal/user/edit")
    public ResponseEntity<?> getPersonalUpdateForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        UserResponse.UpdateDTO respDTO = userService.getPersonalUpdateForm(sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 기업 유저 정보 수정 화면
    @GetMapping("/s/api/company/user/edit")
    public ResponseEntity<?> getCompanyUpdateForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        UserResponse.UpdateDTO respDTO = userService.getCompanyUpdateForm(sessionUser.getId());
        return Resp.ok(respDTO);

    }

}
