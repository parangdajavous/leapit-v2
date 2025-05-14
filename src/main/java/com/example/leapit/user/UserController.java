package com.example.leapit.user;

import com.example.leapit._core.util.Resp;
import jakarta.servlet.http.HttpServletResponse;
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
}