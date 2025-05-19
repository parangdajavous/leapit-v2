package com.example.leapit.board.reply;

import lombok.Data;

import java.sql.Timestamp;

public class ReplyResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String content; // 댓글 내용
        private String name;
        private Integer boardId;
        private Timestamp createdAt;


        public DTO(Reply reply) {
            this.id = reply.getId();
            this.content = reply.getContent();
            this.name = reply.getUser().getName();
            this.boardId = reply.getBoard().getId();
            this.createdAt = reply.getCreatedAt();
        }
    }
}
