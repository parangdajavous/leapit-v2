package com.example.leapit.board.like;

import com.example.leapit._core.util.Resp;
import com.example.leapit.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;
    private final HttpSession session;

    @PostMapping("/s/api/personal/like")
    public ResponseEntity<?> save(@RequestBody LikeRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        LikeResponse.SaveDTO respDTO = likeService.save(reqDTO, sessionUser.getId());

        return Resp.ok(respDTO);
    }

    @DeleteMapping("/s/api/personal/like/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        LikeResponse.DeleteDTO respDTO = likeService.delete(id, sessionUser.getId());   // likeId

        return Resp.ok(respDTO);
    }
}

