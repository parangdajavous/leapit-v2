package com.example.leapit.board;

import com.example.leapit._core.util.JwtUtil;
import com.example.leapit._core.util.Resp;
import com.example.leapit.user.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {
    private final BoardService boardService;
    private final HttpSession session;

    // 게시글 등록
    @PostMapping("/s/api/personal/board")
    public ResponseEntity<?> save(@Valid @RequestBody BoardRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DTO respDTO = boardService.save(reqDTO, sessionUser);

        return Resp.ok(respDTO);
    }

    // 게시글 수정
    @PutMapping("/s/api/personal/board/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @Valid @RequestBody BoardRequest.UpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DTO respDTO = boardService.update(reqDTO, id, sessionUser.getId());

        return Resp.ok(respDTO);
    }

    // 게시글 보기
    @GetMapping("/s/api/personal/board/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DTO respDTO = boardService.getOne(id, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 게시글 목록
    @GetMapping("/api/personal/board")
    public ResponseEntity<?> getList( @RequestHeader(value = "Authorization", required = false) String accessToken) {

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

        List<BoardResponse.ListDTO> respDTO = boardService.getList(userId);
        return Resp.ok(respDTO);
    }

    // 게시글 상세보기
    @GetMapping("/api/personal/board/{id}/detail")
    public ResponseEntity<?> getDetail(@PathVariable("id") Integer id, @RequestHeader(value = "Authorization", required = false) String accessToken) {

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

        BoardResponse.DetailDTO respDTO = boardService.getDetail(id, userId);

        return Resp.ok(respDTO);
    }

    // 게시글 삭제
    @DeleteMapping("/s/api/personal/board/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        boardService.delete(id, sessionUser.getId());

        return Resp.ok(null);
    }


}
