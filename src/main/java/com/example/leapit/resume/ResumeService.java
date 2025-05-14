package com.example.leapit.resume;

import com.example.leapit.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;

    public ResumeResponse.ListDTO getList(Integer userId) {
        List<Resume> resumes = resumeRepository.findAllByUserId(userId);
        return new ResumeResponse.ListDTO(resumes);
    }
}

