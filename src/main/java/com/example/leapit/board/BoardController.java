package com.example.leapit.board;

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

    @PostMapping("/s/api/personal/board")
    public ResponseEntity<?> save(@Valid @RequestBody BoardRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DTO respDTO = boardService.save(reqDTO, sessionUser);

        return Resp.ok(respDTO);
    }


    @PutMapping("/s/api/personal/board/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @Valid @RequestBody BoardRequest.UpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DTO respDTO = boardService.update(reqDTO, id, sessionUser.getId());

        return Resp.ok(respDTO);
    }

    @GetMapping("/s/api/personal/board/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DTO respDTO = boardService.getOne(id, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    @GetMapping("/api/personal/board")
    public ResponseEntity<?> getList() {
        User sessionUser = (User) session.getAttribute("sessionUser");

        Integer sessionUserId = (sessionUser != null) ? sessionUser.getId() : null;

        List<BoardResponse.ListDTO> respDTO = boardService.getList(sessionUserId);
        return Resp.ok(respDTO);
    }

    @GetMapping("/api/personal/board/{id}/detail")
    public ResponseEntity<?> getDetail(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 비로그인 시 상세보기
        Integer sessionUserId = (sessionUser == null ? null : sessionUser.getId());

        BoardResponse.DetailDTO respDTO = boardService.getDetail(id, sessionUserId);

        return Resp.ok(respDTO);
    }

    @DeleteMapping("/s/api/personal/board/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        boardService.delete(id, sessionUser.getId());

        return Resp.ok(null);
    }


}
