package com.example.leapit.companyinfo;

import com.example.leapit._core.error.ex.ExceptionApi400;
import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit._core.util.Base64Util;
import com.example.leapit.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CompanyInfoService {
    private final CompanyInfoRepository companyInfoRepository;

    @Transactional
    public CompanyInfoResponse.DTO save(CompanyInfoRequest.SaveDTO reqDTO, User sessionUser) {

        if (reqDTO.getLogoImageFile() == null || reqDTO.getLogoImageFile().isEmpty()) {
            throw new ExceptionApi400("로고 이미지는 필수입니다.");
        }
        if (reqDTO.getImageFile() == null || reqDTO.getImageFile().isEmpty()) {
            throw new ExceptionApi400("대표이미지는 필수입니다.");
        }


        try {
            // 로고 이미지 저장
            if (reqDTO.getLogoImageFile() != null && !reqDTO.getLogoImageFile().isEmpty()) {
                // logoImageFile의 파일명
                String logoImageFile = reqDTO.getLogoImageFile();

                // 이미지파일을 byte[] 배열로 읽어옴
                byte[] logoImageBytes = Base64Util.readImageAsByteArray(logoImageFile);
                // 읽어온 byte[] 를 Base64 문자열로 변환
                String logoImageString = Base64Util.encodeAsString(logoImageBytes, "image/png");
                // 로고 이미지를 Base64 문자열로 변환한 값을 DTO 필드에 저장 (DB 저장용)
                reqDTO.setLogoImage(logoImageString.substring(0, 100));
            }

            // 대표 이미지 저장
            if (reqDTO.getImageFile() != null && !reqDTO.getImageFile().isEmpty()) {
                // encoding
                String imageFile = reqDTO.getImageFile();

                // 이미지파일을 byte[] 배열로 읽어옴
                byte[] imageBytes = Base64Util.readImageAsByteArray(imageFile);
                // 읽어온 byte[] 를 Base64 문자열로 변환
                String imgString = Base64Util.encodeAsString(imageBytes, "image/png");
                // 로고 이미지를 Base64 문자열로 변환한 값을 DTO 필드에 저장 (DB 저장용)
                reqDTO.setImage(imgString.substring(0, 100));
            }

        } catch (Exception e) {
            throw new ExceptionApi400("파일 업로드 실패");
        }


        CompanyInfo companyInfo = reqDTO.toEntity(sessionUser);
        CompanyInfo companyInfoPS = companyInfoRepository.save(companyInfo);
        return new CompanyInfoResponse.DTO(companyInfoPS);
    }

    @Transactional
    public CompanyInfoResponse.DTO update(Integer id, Integer sessionUserId, CompanyInfoRequest.UpdateDTO reqDTO) {

        CompanyInfo companyInfoPS = companyInfoRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("기업정보를 찾을 수 없습니다."));

        if (!companyInfoPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다.");
        }

        if (reqDTO.getLogoImageFile() == null || reqDTO.getLogoImageFile().isEmpty()) {
            throw new ExceptionApi400("로고 이미지는 필수입니다.");
        }
        if (reqDTO.getImageFile() == null || reqDTO.getImageFile().isEmpty()) {
            throw new ExceptionApi400("대표이미지는 필수입니다.");
        }

        try {
            // 로고 이미지 저장 - encoding
            if (reqDTO.getLogoImageFile() != null && !reqDTO.getLogoImageFile().isEmpty()) {
                // logoImageFile의 파일명
                String logoImageFile = reqDTO.getLogoImageFile();

                // 이미지파일을 byte[] 배열로 읽어옴
                byte[] logoImageBytes = Base64Util.readImageAsByteArray(logoImageFile);
                // 읽어온 byte[] 를 Base64 문자열로 변환
                String logoImageString = Base64Util.encodeAsString(logoImageBytes, "image/png");
                // 로고 이미지를 Base64 문자열로 변환한 값을 DTO 필드에 저장 (DB 저장용)
                reqDTO.setLogoImage(logoImageString.substring(0, 100));
            }

            // 대표 이미지 저장 - encoding
            if (reqDTO.getImageFile() != null && !reqDTO.getImageFile().isEmpty()) {
                // imageFile의 파일명
                String imageFile = reqDTO.getImageFile();

                // 이미지파일을 byte[] 배열로 읽어옴
                byte[] imageBytes = Base64Util.readImageAsByteArray(imageFile);
                // 읽어온 byte[] 를 Base64 문자열로 변환
                String imgString = Base64Util.encodeAsString(imageBytes, "image/png");
                // 로고 이미지를 Base64 문자열로 변환한 값을 DTO 필드에 저장 (DB 저장용)
                reqDTO.setImage(imgString.substring(0, 100));
            }

        } catch (Exception e) {
            throw new ExceptionApi400("파일 업로드 실패");
        }


        // 엔티티 업데이트 메서드 호출
        companyInfoPS.update(
                reqDTO.getLogoImage(),
                reqDTO.getCompanyName(),
                reqDTO.getEstablishmentDate(),
                reqDTO.getAddress(),
                reqDTO.getMainService(),
                reqDTO.getIntroduction(),
                reqDTO.getImage(),
                reqDTO.getBenefit()
        );

        return new CompanyInfoResponse.DTO(companyInfoPS);
    }

    public CompanyInfoResponse.DTO UpdateAndReturn(Integer id, Integer sessionUserId) {
        CompanyInfo companyInfoPS = companyInfoRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("기업정보를 찾을 수 없습니다."));

        if (!companyInfoPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }
        return new CompanyInfoResponse.DTO(companyInfoPS);
    }
}

