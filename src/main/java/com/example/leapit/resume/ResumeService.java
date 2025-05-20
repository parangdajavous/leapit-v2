package com.example.leapit.resume;

import com.example.leapit._core.error.ex.ExceptionApi400;
import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit._core.util.Base64Util;
import com.example.leapit.application.Application;
import com.example.leapit.application.ApplicationRepository;
import com.example.leapit.common.enums.CareerLevel;
import com.example.leapit.common.enums.EducationLevel;
import com.example.leapit.common.enums.EtcType;
import com.example.leapit.common.positiontype.PositionTypeRepository;
import com.example.leapit.common.techstack.TechStackRepository;
import com.example.leapit.resume.education.EducationService;
import com.example.leapit.resume.etc.EtcService;
import com.example.leapit.resume.experience.ExperienceService;
import com.example.leapit.resume.link.LinkService;
import com.example.leapit.resume.project.ProjectService;
import com.example.leapit.resume.techstack.ResumeTechStackService;
import com.example.leapit.resume.training.TrainingService;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.UUID;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final ApplicationRepository applicationRepository;
    private final PositionTypeRepository positionTypeRepository;
    private final TechStackRepository techStackRepository;
    private final UserRepository userRepository;

    private final ResumeTechStackService resumeTechStackService;
    private final LinkService linkService;
    private final EducationService educationService;
    private final ExperienceService experienceService;
    private final ProjectService projectService;
    private final TrainingService trainingService;
    private final EtcService etcService;

    // 이력서 목록
    public ResumeResponse.ListDTO getList(Integer userId) {
        List<Resume> resumes = resumeRepository.findAllByUserId(userId);
        return new ResumeResponse.ListDTO(resumes);
    }

    // 이력서 삭제
    @Transactional
    public void delete(Integer resumeId, Integer sessionUserId) {
        // 1. 이력서 존재 확인
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ExceptionApi404("이력서가 존재하지 않습니다."));

        // 2. 해당 이력서로 된 지원 여부 확인
        List<Application> applications = applicationRepository.findAllByResumeId(resumeId);
        if (applications != null && !applications.isEmpty()) {
            throw new ExceptionApi400("이 이력서는 지원 이력이 있어 삭제할 수 없습니다.");
        }

        // 3. 이력서 권한 확인
        if (!(resume.getUser().getId().equals(sessionUserId))) {
            throw new ExceptionApi403("해당 이력서에 대한 권한이 없습니다.");
        }

        // 4. 이력서 삭제
        resumeRepository.deleteById(resumeId);
    }

    // 이력서 등록 화면
    public ResumeResponse.SaveDTO getSaveForm(Integer sessionUserId) {
        //List<CareerLevel> careerLevels = List.of(CareerLevel.values());
        List<EducationLevel> educationLevels = List.of(EducationLevel.values());
        List<EtcType> etcTypes = List.of(EtcType.values());
        List<String> positionTypes = positionTypeRepository.findAll();
        List<String> techStacks = techStackRepository.findAll();

        // sessionUser에서 brithDate, email 등의 정보를 꺼내올 수 없음 -> user 조회
        User userPS = userRepository.findById(sessionUserId)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        return new ResumeResponse.SaveDTO(userPS, educationLevels, etcTypes, positionTypes, techStacks);
    }

    // 이력서 등록
    @Transactional
    public ResumeResponse.DTO save(ResumeRequest.SaveDTO reqDTO, User sessionUser) {
        // 이미지
        String base64EncodedImg = reqDTO.getPhotoUrl(); // Base64 인코딩된 문자열
        String originalFileName = reqDTO.getPhotoFileName();

        // 디코딩 -> DB에 파일명 저장, upload 폴더에 실제 이미지
        if (base64EncodedImg != null && !base64EncodedImg.isEmpty()) {
            try {
                // 디렉토리 생성
                String uploadDir = System.getProperty("user.dir") + "/upload/";
                Files.createDirectories(Paths.get(uploadDir));

                // MIME 타입 추출 및 확장자 결정
                String mimeType = Base64Util.getMimeType(base64EncodedImg); // 예: jpeg, png
                String extension;
                if (mimeType.equals("jpeg")) {
                    extension = ".jpg";
                } else if (mimeType.equals("png")) {
                    extension = ".png";
                } else {
                    throw new ExceptionApi400("지원하지 않는 이미지 형식입니다. jpeg, png만 허용됩니다.");
                }

                // 고유한 파일명 생성 및 저장
                String imageFilename = UUID.randomUUID() + "_" + originalFileName;
                Path imagePath = Paths.get(uploadDir + imageFilename);

                // 이미지 디코딩 및 저장
                byte[] imageBytes = Base64Util.decodeAsBytes(base64EncodedImg);
                Files.write(imagePath, imageBytes);

                // DTO에 파일명 설정 (DB 저장용)
                reqDTO.setPhotoUrl(imageFilename);

            } catch (Exception e) {
                throw new RuntimeException("이미지 디코딩 및 저장 실패", e);
            }
        }

        Resume resume = reqDTO.toEntity(sessionUser);
        Resume resumePS = resumeRepository.save(resume);
        return new ResumeResponse.DTO(resumePS);
    }

    // 이력서 수정 화면
    public ResumeResponse.UpdateDTO getUpdateForm(Integer resumeId, Integer sessionUserId) {
        // 1. 이력서 존재 확인
        Resume resumePS = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ExceptionApi404("이력서를 찾을 수 없습니다"));

        // 2. 권한 확인
        if (!(resumePS.getUser().getId().equals(sessionUserId))) {
            throw new ExceptionApi403("해당 이력서에 대한 권한이 없습니다.");
        }

        //List<CareerLevel> careerLevels = List.of(CareerLevel.values());
        List<EducationLevel> educationLevels = List.of(EducationLevel.values());
        List<EtcType> etcTypes = List.of(EtcType.values());
        List<String> positionTypes = positionTypeRepository.findAll();
        List<String> techStacks = techStackRepository.findAll();

        // sessionUser에서 brithDate, email 등의 정보를 꺼내올 수 없음 -> user 조회
        User userPS = userRepository.findById(sessionUserId)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        return new ResumeResponse.UpdateDTO(userPS, educationLevels, etcTypes, positionTypes, techStacks, new ResumeResponse.DTO(resumePS));
    }

    // 이력서 수정
    @Transactional
    public ResumeResponse.DTO update(Integer resumeId, ResumeRequest.UpdateDTO reqDTO, Integer sessionUserId) {
        // 1. 이력서 존재 확인
        Resume resumePS = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ExceptionApi404("이력서를 찾을 수 없습니다"));

        // 2. 권한 확인
        if (!(resumePS.getUser().getId().equals(sessionUserId))) {
            throw new ExceptionApi403("해당 이력서에 대한 권한이 없습니다.");
        }

        // 이미지
        String base64EncodedImg = reqDTO.getPhotoUrl(); // Base64 인코딩된 문자열
        String originalFileName = reqDTO.getPhotoFileName();

        // 디코딩 -> DB에 파일명 저장, upload 폴더에 실제 이미지
        if (base64EncodedImg != null && !base64EncodedImg.isEmpty()) {
            try {
                // 디렉토리 생성
                String uploadDir = System.getProperty("user.dir") + "/upload/";
                Files.createDirectories(Paths.get(uploadDir));

                // MIME 타입 추출 및 확장자 결정
                String mimeType = Base64Util.getMimeType(base64EncodedImg); // 예: jpeg, png
                String extension;
                if (mimeType.equals("jpeg")) {
                    extension = ".jpg";
                } else if (mimeType.equals("png")) {
                    extension = ".png";
                } else {
                    throw new ExceptionApi400("지원하지 않는 이미지 형식입니다. jpeg, png만 허용됩니다.");
                }

                // 고유한 파일명 생성 및 저장
                String imageFilename = UUID.randomUUID() + "_" + originalFileName;
                Path imagePath = Paths.get(uploadDir + imageFilename);

                // 이미지 디코딩 및 저장
                byte[] imageBytes = Base64Util.decodeAsBytes(base64EncodedImg);
                Files.write(imagePath, imageBytes);

                // DTO에 파일명 설정 (DB 저장용)
                reqDTO.setPhotoUrl(imageFilename);

            } catch (Exception e) {
                throw new RuntimeException("이미지 디코딩 및 저장 실패", e);
            }
        }

        // 3. 업데이트
        // 이력서 업데이트
        resumePS.update(reqDTO.getTitle(), reqDTO.getPhotoUrl(), reqDTO.getSummary(), reqDTO.getPositionType(), reqDTO.getSelfIntroduction());

        // 항목별 업데이트는 각 항목의 Service 호출
        resumeTechStackService.update(resumePS, reqDTO.getResumeTechStacks());
        linkService.update(resumePS, reqDTO.getLinks());
        educationService.update(resumePS, reqDTO.getEducations());
        experienceService.update(resumePS, reqDTO.getExperiences());
        projectService.update(resumePS, reqDTO.getProjects());
        trainingService.update(resumePS, reqDTO.getTrainings());
        etcService.update(resumePS, reqDTO.getEtcs());

        return new ResumeResponse.DTO(resumePS);
    }
}

