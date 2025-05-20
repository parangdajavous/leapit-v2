package com.example.leapit.common.positiontype;

import lombok.Data;

public class PositionTypeResponse {

    @Data
    public static class DTO {
        private String code;

        public DTO(String code) {
            this.code = code;
        }
    }
}
