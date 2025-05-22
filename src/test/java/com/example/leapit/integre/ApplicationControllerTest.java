package com.example.leapit.integre;

import com.example.leapit.MyRestDoc;
import com.example.leapit._core.util.JwtUtil;
import com.example.leapit.application.ApplicationRequest;
import com.example.leapit.common.enums.Role;
import com.example.leapit.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ApplicationControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    private String personalAccessToken;

    private String companyAccessToken;

    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
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
    public void get_apply_form_test() throws Exception {
        // given
        Integer jobPostingId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/personal/jobposting/{id}/apply", jobPostingId)
                        .header("Authorization", "Bearer " + personalAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[0].resumeId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[0].resumeTitle").value("쌀의 이력서"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[0].techStacks[0]").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[0].techStacks[1]").value("Spring Boot"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[0].positionType").value("백엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[0].registeredDate").value(
                Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}$")
        ));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[1].resumeId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[1].resumeTitle").value("쌀의 이력서2"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[1].techStacks[0]").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[1].positionType").value("프론트엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumes[0].registeredDate").value(
                Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}$")
        ));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingTitle").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userEmail").value("ssar@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.contactNumber").value("010-1234-5678"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.birthDate")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }


    @Test
    public void save_test() throws Exception {
        // given
        ApplicationRequest.SaveDTO reqDTO = new ApplicationRequest.SaveDTO();
        reqDTO.setJobPostingId(3);
        reqDTO.setResumeId(2);

        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/personal/application")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("Authorization", "Bearer " + personalAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.appliedDate")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }
}
