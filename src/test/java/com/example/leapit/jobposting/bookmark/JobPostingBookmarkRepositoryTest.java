package com.example.leapit.jobposting.bookmark;

import com.example.leapit.jobposting.JobPosting;
import com.example.leapit.jobposting.JobPostingRepository;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

@Import({JobPostingBookmarkRepository.class, UserRepository.class, JobPostingRepository.class})
@DataJpaTest
public class JobPostingBookmarkRepositoryTest {

    @Autowired
    private JobPostingBookmarkRepository jobPostingBookmarkRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Test
    public void find_items_by_user_id_test() {
        // given
        Integer userId = 1;

        // when
        List<JobPostingBookmarkResponse.ItemDTO> bookmarks =
                jobPostingBookmarkRepository.findItemsByuserId(userId);

        // eye
        System.out.println("=========개인 스크랩 현황 테스트=========");
        System.out.println("스크랩한 공고 수: " + bookmarks.size());
        for (JobPostingBookmarkResponse.ItemDTO dto : bookmarks) {
            System.out.println("공고 ID: " + dto.getJobPostingId());
            System.out.println("회사명: " + dto.getJobPostingTitle());
            System.out.println("공고 제목: " + dto.getCompanyName());
            System.out.println("마감일: " + dto.getDeadLine());
            System.out.println("=========== 끝 ===========");
        }
    }

    @Test
    public void findByUserIdAndJobPostingId_whenBookmarkExists() {
        // given
        Integer userId = 1;
        Integer jobPostingId = 6;

        User user = userRepository.findById(userId).orElseThrow();
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId).orElseThrow();

        JobPostingBookmark bookmark = JobPostingBookmark.builder()
                .user(user)
                .jobPosting(jobPosting)
                .build();

        jobPostingBookmarkRepository.save(bookmark);

        // when
        Optional<JobPostingBookmark> result =
                jobPostingBookmarkRepository.findByUserIdAndJobPostingId(user.getId(), jobPosting.getId());

        // eye
        if (result.isPresent()) {
            System.out.println("Bookmark found: ID = " + result.get().getId());
        } else {
            System.out.println("Bookmark not found (unexpected)");
        }
    }

    @Test
    public void findByUserIdAndJobPostingId_whenBookmarkDoesNotExist() {
        // given
        Integer userId = 999;
        Integer jobPostingId = 888;

        // when
        Optional<JobPostingBookmark> result = jobPostingBookmarkRepository.findByUserIdAndJobPostingId(userId, jobPostingId); // 존재하지 않는 ID

        // eye
        if (result.isEmpty()) {
            System.out.println("Bookmark not found as expected");
        } else {
            System.out.println("Bookmark found (unexpected): ID = " + result.get().getId());
        }
    }
}
