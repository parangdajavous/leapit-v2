package com.example.leapit.companyinfo;

import com.example.leapit.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;

public class CompanyInfoRequest {

    @Data
    public static class SaveDTO {
        private Integer id;

        // Base64로 변환된 로고 이미지 문자열 (data:image/png;base64,...)
        private String logoImage;

        @NotEmpty(message = "회사명은 필수입니다.")
        private String companyName;

        private LocalDate establishmentDate;

        @NotEmpty(message = "주소는 필수입니다.")
        private String address;

        private String mainService;
        private String introduction;

        // Base64로 변환된 대표 이미지 문자열 (data:image/png;base64,...)
        private String image;

        private String benefit;

        // base64
        private String logoImageFile;
        private String imageFile;

        public CompanyInfo toEntity(User user) {
            return CompanyInfo.builder()
                    .logoImage(logoImage) // Base64 문자열이 들어감
                    .companyName(companyName)
                    .establishmentDate(establishmentDate)
                    .address(address)
                    .mainService(mainService)
                    .introduction(introduction)
                    .image(image) // Base64 문자열이 들어감
                    .benefit(benefit)
                    .user(user)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        private String logoImage;  // Base64로 변환된 로고 이미지 문자열 (data:image/png;base64,...)
        @NotEmpty(message = "회사명은 필수입니다.")
        private String companyName;
        private LocalDate establishmentDate;
        @NotEmpty(message = "주소는 필수입니다.")
        private String address;
        private String mainService;
        private String introduction;
        private String image;  // Base64로 변환된 대표 이미지 문자열 (data:image/png;base64,...)
        private String benefit;

        // Base64로 변환할 이미지 파일
        private String logoImageFile;
        private String imageFile;
    }

}
