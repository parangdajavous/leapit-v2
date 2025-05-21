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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CompanyInfoService {
    private final CompanyInfoRepository companyInfoRepository;
    private final JobPostingRepository jobPostingRepository;

    // 기업정보 등록
    @Transactional
    public CompanyInfoResponse.DTO save(CompanyInfoRequest.SaveDTO reqDTO, User sessionUser) {

        if (reqDTO.getLogoImageFileContent() == null || reqDTO.getLogoImageFileContent().isEmpty()) {
            throw new ExceptionApi400("로고 이미지는 필수입니다.");
        }
        if (reqDTO.getImageFileContent() == null || reqDTO.getImageFileContent().isEmpty()) {
            throw new ExceptionApi400("대표이미지는 필수입니다.");
        }


        try {
            // 로고 이미지 처리
            if (reqDTO.getLogoImageFileContent() != null && !reqDTO.getLogoImageFileContent().isEmpty()) {
                // decoding
                byte[] logoImageBytes = Base64Util.decodeAsBytes(reqDTO.getLogoImageFileContent());

                // 고유 파일명 생성
                String logoImage = UUID.randomUUID() + "_logo.png";
                String uploadDir = System.getProperty("user.dir") + "/upload/";
                Path logoPath = Paths.get(uploadDir + logoImage);

                Files.write(logoPath, logoImageBytes); // 저장

                reqDTO.setLogoImage(logoImage); // DB에는 파일명만 저장
            }

            // 대표 이미지 처리
            if (reqDTO.getImageFileContent() != null && !reqDTO.getImageFileContent().isEmpty()) {
                byte[] imageBytes = Base64Util.decodeAsBytes(reqDTO.getImageFileContent());

                String image = UUID.randomUUID() + "_image.png";
                String uploadDir = System.getProperty("user.dir") + "/upload/";
                Path imagePath = Paths.get(uploadDir + image);

                Files.write(imagePath, imageBytes);

                reqDTO.setImage(image);
            }

        } catch (Exception e) {
            throw new ExceptionApi400("파일 저장 실패");
        }


        CompanyInfo companyInfo = reqDTO.toEntity(sessionUser);
        CompanyInfo companyInfoPS = companyInfoRepository.save(companyInfo);
        return new CompanyInfoResponse.DTO(companyInfoPS);
    }

    // 기업정보 수정
    @Transactional
    public CompanyInfoResponse.DTO update(Integer id, Integer sessionUserId, CompanyInfoRequest.UpdateDTO reqDTO) {

        CompanyInfo companyInfoPS = companyInfoRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("기업정보를 찾을 수 없습니다."));

        if (!companyInfoPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다.");
        }

        if (reqDTO.getLogoImageFileContent() == null || reqDTO.getLogoImageFileContent().isEmpty()) {
            throw new ExceptionApi400("로고 이미지는 필수입니다.");
        }
        if (reqDTO.getImageFileContent() == null || reqDTO.getImageFileContent().isEmpty()) {
            throw new ExceptionApi400("대표이미지는 필수입니다.");
        }


        try {
            // 로고 이미지 처리
            if (reqDTO.getLogoImageFileContent() != null && !reqDTO.getLogoImageFileContent().isEmpty()) {
                // decoding
                byte[] logoImageBytes = Base64Util.decodeAsBytes(reqDTO.getLogoImageFileContent());

                // 고유 파일명 생성
                String logoImage = UUID.randomUUID() + "_logo.png";
                String uploadDir = System.getProperty("user.dir") + "/upload/";
                Path logoPath = Paths.get(uploadDir + logoImage);

                Files.write(logoPath, logoImageBytes); // 저장

                reqDTO.setLogoImage(logoImage); // DB에는 파일명만 저장
            }

            // 대표 이미지 처리
            if (reqDTO.getImageFileContent() != null && !reqDTO.getImageFileContent().isEmpty()) {
                byte[] imageBytes = Base64Util.decodeAsBytes(reqDTO.getImageFileContent());

                String image = UUID.randomUUID() + "_image.png";
                String uploadDir = System.getProperty("user.dir") + "/upload/";
                Path imagePath = Paths.get(uploadDir + image);

                Files.write(imagePath, imageBytes);

                reqDTO.setImage(image);
            }

        } catch (Exception e) {
            throw new ExceptionApi400("파일 저장 실패");
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

    // 기업정보 보기
    public CompanyInfoResponse.DTO getOne(Integer id, Integer sessionUserId) {
        CompanyInfo companyInfoPS = companyInfoRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("기업정보를 찾을 수 없습니다."));

        if (!companyInfoPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }
        return new CompanyInfoResponse.DTO(companyInfoPS);
    }

    // 기업정보 상세보기
    public CompanyInfoResponse.DetailDTO getDetail(Integer id, Integer userId) {
        CompanyInfo companyInfoPS = companyInfoRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("기업정보를 찾을 수 없습니다"));

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
        return new CompanyInfoResponse.DetailDTO(companyInfoPS, userId, jobPostingCount.intValue(), jobPostings, allTechStacks);
    }

    // 기업정보 조회
    public CompanyInfo findById(Integer id) {
        return companyInfoRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("기업정보를 찾을 수 없습니다."));
    }

}

