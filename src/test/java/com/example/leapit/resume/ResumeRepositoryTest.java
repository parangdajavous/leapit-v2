package com.example.leapit.resume;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(ResumeRepository.class)
@DataJpaTest
public class ResumeRepositoryTest {
    @Autowired
    private ResumeRepository resumeRepository;

    @Test
    public void findAllByUserId() {
        // given
        Integer userId = 1;

        // when
        List<Resume> resumes = resumeRepository.findAllByUserId(userId);

        // eye
        for (Resume resume : resumes) {
            System.out.println("id: " + resume.getId());
            System.out.println("userId: " + resume.getUser().getId());
            System.out.println("title: " + resume.getTitle());
            System.out.println("photoUrl: " + resume.getPhotoUrl());
            System.out.println("summary: " + resume.getSummary());
            System.out.println("positionType: " + resume.getPositionType());
            System.out.println("selfIntroduction: " + resume.getSelfIntroduction());
            System.out.println("createAt: " + resume.getCreatedAt());
            System.out.println("updatedAt: " + resume.getUpdatedAt());
            System.out.println();
        }
    }

    @Test
    public void deleteById() {
        // given
        Integer resumeId = 1;
        Integer userId = 1;

        // eye
        List<Resume> beforResumes = resumeRepository.findAllByUserId(userId);
        for (Resume resume : beforResumes) {
            System.out.println("id: " + resume.getId());
        }
        System.out.println();

        // when
        resumeRepository.deleteById(resumeId);

        // eye
        List<Resume> afterResumes = resumeRepository.findAllByUserId(userId);
        for (Resume resume : afterResumes) {
            System.out.println("id: " + resume.getId());
        }
        System.out.println();
    }
}
