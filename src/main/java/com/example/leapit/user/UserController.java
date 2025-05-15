package com.example.leapit.user;

import com.example.leapit._core.error.ex.ExceptionApi400;
import com.example.leapit._core.util.Resp;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    @PostMapping("/personal/join")
    public ResponseEntity<?> personalJoin(@Valid @RequestBody UserRequest.PersonalJoinDTO reqDTO, Errors errors) {
        UserResponse.DTO respDTO = userService.join(reqDTO);
        return Resp.ok(respDTO);
    }

    @PostMapping("/company/join")
    public ResponseEntity<?> companyJoin(@Valid @RequestBody UserRequest.CompanyJoinDTO reqDTO) {
        UserResponse.DTO respDTO = userService.join(reqDTO);
        return Resp.ok(respDTO);
    }

    @GetMapping("/api/check-username-available/{username}")
    public ResponseEntity<?> checkUsernameAvailable(@PathVariable("username") String username) {
        Map<String, Object> respDTO = userService.checkUsernameAvailable(username);
        return Resp.ok(respDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest.LoginDTO loginDTO, HttpServletResponse response, Errors errors) {
        UserResponse.TokenDTO respDTO = userService.login(loginDTO);
        return Resp.ok(respDTO);
    }

    @PutMapping("/s/company/user")
    public ResponseEntity<?> companyUpdate(@Valid @RequestBody UserRequest.CompanyUpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (!reqDTO.getNewPassword().equals(reqDTO.getConfirmPassword())) throw new ExceptionApi400("입력한 비밀번호가 다릅니다.");
        UserResponse.UpdateDTO respDTO = userService.update(reqDTO, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    @PutMapping("/s/personal/user")
    public ResponseEntity<?> personalUpdate(@Valid @RequestBody UserRequest.PersonalUpdateDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (!reqDTO.getNewPassword().equals(reqDTO.getConfirmPassword())) throw new ExceptionApi400("입력한 비밀번호가 다릅니다.");
        UserResponse.UpdateDTO respDTO = userService.update(reqDTO, sessionUser.getId());
        return Resp.ok(respDTO);
    }
}
