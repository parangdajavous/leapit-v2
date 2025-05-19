package com.example.leapit.companyinfo;

import com.example.leapit._core.error.ex.ExceptionApi400;
import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit._core.util.Base64Util;
import com.example.leapit.jobposting.JobPosting;
import com.example.leapit.jobposting.JobPostingRepository;
import com.example.leapit.jobposting.techstack.JobPostingTechStack;
import com.example.leapit.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CompanyInfoService {
    private final CompanyInfoRepository companyInfoRepository;
    private final JobPostingRepository jobPostingRepository;

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
                reqDTO.setLogoImage(logoImageString);
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
                reqDTO.setImage(imgString);
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
                reqDTO.setLogoImage(logoImageString);
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
                reqDTO.setImage(imgString);
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

    public CompanyInfoResponse.DTO getOne(Integer id, Integer sessionUserId) {
        CompanyInfo companyInfoPS = companyInfoRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("기업정보를 찾을 수 없습니다."));

        if (!companyInfoPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }
        return new CompanyInfoResponse.DTO(companyInfoPS);
    }

    public CompanyInfoResponse.DetailDTO getDetail(Integer id, Integer userId) {
        CompanyInfo companyInfoPS = companyInfoRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("기업정보를 찾을 수 없습니다"));

        // 1. DB에 있는 logoImage, image를 Base64 문자열로 변환
        String logoImageString = null;
        String imageString = null;
        try {
            // logoImage Base64 변환
            if (companyInfoPS.getLogoImage() != null) {
                String logoImage = companyInfoPS.getLogoImage();
                byte[] logoImageBytes = Base64Util.readImageAsByteArray(logoImage);
                logoImageString = Base64Util.encodeAsString(logoImageBytes, "image/png");
            }

            // image Base64 변환
            if (companyInfoPS.getImage() != null) {
                String image = companyInfoPS.getImage();
                byte[] imageBytes = Base64Util.readImageAsByteArray(image);
                imageString = Base64Util.encodeAsString(imageBytes, "image/png");
            }
        } catch (Exception e) {
            throw new ExceptionApi400("파일 읽기 실패");
        }

        // 2. 조인된 결과 가져오기 (JobPosting + JobPostingTechStack)
        List<Object[]> results = jobPostingRepository.findByUserIdJoinJobPostingTechStacks(userId);

        // 3. 공고 Map과 기술스택 Map 생성
        Map<Integer, JobPosting> postingMap = new HashMap<>();
        List<JobPostingTechStack> allTechStacks = new ArrayList<>();

        for (Object[] row : results) {
            JobPosting jobPosting = (JobPosting) row[0];
            JobPostingTechStack techStack = (JobPostingTechStack) row[1];

            postingMap.putIfAbsent(jobPosting.getId(), jobPosting);
            if (techStack != null) {
                allTechStacks.add(techStack);
            }
        }


        // 4. 중복 제거 후 상위 2개 ID만 추출
        List<Integer> top2PostingIds = results.stream()
                .map(row -> ((JobPosting) row[0]).getId())
                .distinct()
                .limit(2)
                .toList();


        // 5. 해당 ID에 맞는 JobPosting 리스트만 추출
        List<JobPosting> jobPostings = top2PostingIds.stream()
                .map(postingMap::get)
                .toList();


        for (JobPosting jobPosting : jobPostings) {
            // 마감일이 지난 공고는 제외
            if (jobPosting.getDeadline() != null && jobPosting.getDeadline().isBefore(LocalDate.now())) {
                continue;
            }
        }

        // 6. 마감일이 지나지 않은 공고 수 계산
        Long jobPostingCount = jobPostingRepository.countByUserIdAndDeadlineAfter(userId);

        // 7. DTO 생성자 호출
        return new CompanyInfoResponse.DetailDTO(companyInfoPS, userId, jobPostingCount.intValue(), jobPostings, allTechStacks, logoImageString, imageString);
    }

    public CompanyInfo findById(Integer id) {
        return companyInfoRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("기업정보를 찾을 수 없습니다."));
    }

}

