package com.example.leapit.companyinfo;

import com.example.leapit.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;

public class CompanyInfoRequest {

    @Data
    public static class SaveDTO {
        private Integer id;

        private String logoImage; // 로고이미지 파일명

        @NotEmpty(message = "회사명은 필수입니다.")
        private String companyName;

        private LocalDate establishmentDate;

        @NotEmpty(message = "주소는 필수입니다.")
        private String address;

        private String mainService;
        private String introduction;


        private String image; // 대표 이미지 파일명

        private String benefit;

        // base64로 변환된 문자열
        private String logoImageFileContent;
        private String imageFileContent;

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
        private String logoImage;  // 로고 이미지 파일명
        @NotEmpty(message = "회사명은 필수입니다.")
        private String companyName;
        private LocalDate establishmentDate;
        @NotEmpty(message = "주소는 필수입니다.")
        private String address;
        private String mainService;
        private String introduction;
        private String image;  // 대표 이미지 파일명
        private String benefit;

        // base64로 변환된 문자열
        private String logoImageFileContent;
        private String imageFileContent;
    }

}
