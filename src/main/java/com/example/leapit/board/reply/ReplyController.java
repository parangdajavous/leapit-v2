package com.example.leapit.board.reply;

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
public class ReplyController {
    private final ReplyService replyService;
    private final HttpSession session;

    @PostMapping("/s/api/personal/reply")
    public ResponseEntity<?> save(@Valid @RequestBody ReplyRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        ReplyResponse.DTO respDTO = replyService.save(reqDTO, sessionUser);

        return Resp.ok(respDTO);
    }

    @DeleteMapping("/s/api/personal/reply/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        replyService.delete(id, sessionUser.getId());
        return Resp.ok(null);
    }
}
