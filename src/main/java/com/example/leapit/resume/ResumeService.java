package com.example.leapit.resume;

import com.example.leapit._core.error.ex.ExceptionApi400;
import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit.application.Application;
import com.example.leapit.application.ApplicationRepository;
import com.example.leapit.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final ApplicationRepository applicationRepository;

    public ResumeResponse.ListDTO getList(Integer userId) {
        List<Resume> resumes = resumeRepository.findAllByUserId(userId);
        return new ResumeResponse.ListDTO(resumes);
    }

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
}

