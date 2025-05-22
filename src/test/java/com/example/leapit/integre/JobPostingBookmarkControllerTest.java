package com.example.leapit.integre;

import com.example.leapit.MyRestDoc;
import com.example.leapit._core.util.JwtUtil;
import com.example.leapit.common.enums.CareerLevel;
import com.example.leapit.common.enums.EducationLevel;
import com.example.leapit.common.enums.Role;
import com.example.leapit.jobposting.JobPostingRequest;
import com.example.leapit.jobposting.bookmark.JobPostingBookmarkRequest;
import com.example.leapit.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.nullValue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class JobPostingBookmarkControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    private String personalAccessToken;

    private String companyAccessToken;

    @BeforeEach
    public void setUp() {
        System.out.println("setUp");
        User ssar = User.builder()
                .id(1)
                .username("ssar")
                .role(Role.valueOf("PERSONAL"))
                .build();
        personalAccessToken = JwtUtil.create(ssar);

        User company01 = User.builder()
                .id(6)
                .username("company01")
                .role(Role.valueOf("COMPANY"))
                .build();
        companyAccessToken = JwtUtil.create(company01);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    public void get_my_bookmark_test() throws Exception {
        // given
        Integer userId = 1;

        String requestBody = om.writeValueAsString(userId);
//        System.out.println("requestBody: " + requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/s/personal/mypage/bookmark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + personalAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("====================================");
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

        // 첫 번째 북마크 확인
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[0].jobPostingId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[0].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[0].jobPostingTitle").value("데이터 엔지니어 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[0].deadLine").value("2025-07-20"));

        // 두 번째 북마크 확인
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[1].jobPostingId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[1].companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[1].jobPostingTitle").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[1].deadLine").value("2025-06-30"));

        // 세 번째 북마크 확인
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[2].jobPostingId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[2].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[2].jobPostingTitle").value("프론트엔드 개발자 모집"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarks[2].deadLine").value("2025-05-20"));

        // 상태 요약 확인
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.status.total").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.status.passed").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.status.failed").value(0));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void save_test() throws Exception {
        // given
        JobPostingBookmarkRequest.SaveDTO reqDTO = new JobPostingBookmarkRequest.SaveDTO();
        reqDTO.setJobPostingId(1);

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println("requestBody: " + requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.post("/s/api/personal/jobpostingbookmark")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + personalAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("====================================");
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarkId").value(6));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void delete_test() throws Exception {
        // given
        Integer jobPostingBookmarkId = 1;

        String requestBody = om.writeValueAsString(jobPostingBookmarkId);
//        System.out.println("requestBody: " + requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.delete("/s/api/personal/jobpostingbookmark/{id}", jobPostingBookmarkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + personalAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("====================================");
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body").value(nullValue()));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
