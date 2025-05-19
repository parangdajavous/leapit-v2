package com.example.leapit.jobposting.bookmark;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(JobPostingBookmarkRepository.class)
@DataJpaTest
public class JobPostingBookmarkRepositoryTest {

    @Autowired
    private JobPostingBookmarkRepository jobPostingBookmarkRepository;

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

}
