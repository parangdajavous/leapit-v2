package com.example.leapit.integre;

import com.example.leapit.MyRestDoc;
import com.example.leapit._core.util.JwtUtil;
import com.example.leapit.application.ApplicationRequest;
import com.example.leapit.common.enums.PassStatus;
import com.example.leapit.common.enums.Role;
import com.example.leapit.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ApplicationControllerTest extends MyRestDoc {
    @Autowired
    private ObjectMapper om;

    private String personalAccessToken;
    private String companyAccessToken;

    @BeforeEach
    public void setUp(){
        // 테스트 시작 전에 실행할 코드
        User ssar = User.builder().id(1).username("ssar").role(Role.valueOf("PERSONAL")).build();
        User company01 = User.builder().id(6).username("company01").role(Role.valueOf("COMPANY")).build();
        personalAccessToken = JwtUtil.create(ssar);
        companyAccessToken = JwtUtil.create(company01);
    }

    @AfterEach
    public void tearDown(){
        // 테스트 후 정리할 코드
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

    // 지원서 합격,불합격 처리
    @Test
    public void update_pass_test() throws Exception{
        // given
        Integer applicationId = 4;

        ApplicationRequest.UpdatePassDTO reqDTO = new ApplicationRequest.UpdatePassDTO();
        reqDTO.setPassStatus(PassStatus.valueOf("FAIL"));

        String requestBody = om.writeValueAsString(reqDTO);
        // System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/company/applicant/{id}/pass", applicationId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + companyAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        // System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.applicationId").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.passStatus").value("FAIL"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void get_detail_test() throws Exception{
        // given
        Integer applicationId = 4;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/company/applicant/{id}", applicationId)
                        .header("Authorization", "Bearer " + companyAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        // System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.passStatus").value("FAIL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.viewStatus").value("VIEWED"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.bookmarkStatus").value("NOT_BOOKMARKED"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingTitle").value("시니어 백엔드 개발자 채용"));
        // 이력서
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.id").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.title").value("파이썬 이력서"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.photoUrl").value(Matchers.matchesPattern("data:(png|jpeg);base64,[A-Za-z0-9+/=\\r\\n]+$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.summary").value("Django와 FastAPI 경험 있음"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.positionType").value("백엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.selfIntroduction").value("데이터 파이프라인 경험"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.createdAt").value(Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}.*")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.updatedAt").value(Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}.*")));
        // resumeTechStacks
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.resumeTechStacks[0]").value("Python"));
        // educations
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.educations[0].id").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.educations[0].graduationDate").value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.educations[0].isDropout").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.educations[0].educationLevel").value("HIGH_SCHOOL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.educations[0].schoolName").value("라마바고등학교"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.educations[0].major").value("정보처리학과"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.educations[0].gpa").value("3.50"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.educations[0].gpaScale").value("4.5"));
        // projects
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.projects[0].id").value(5));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.projects[0].startDate")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.projects[0].endDate")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.projects[0].isOngoing").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.projects[0].title").value("쇼핑몰 프로젝트"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.projects[0].summary").value("온라인 쇼핑몰 구축"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.projects[0].description").value("Spring Boot + Thymeleaf 기반 쇼핑몰 프로젝트입니다. 장바구니, 결제 연동 기능 포함."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.projects[0].repositoryUrl").value("https://github.com/example5"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.projects[0].techStacks[0]").value("Kotlin"));
        // experiences (없음)
        // links (없음)
        // trainings
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.trainings[0].id").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.trainings[0].startDate")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.trainings[0].endDate")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.trainings[0].isOngoing").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.trainings[0].courseName").value("데이터 분석 입문"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.trainings[0].institutionName").value("패스트캠퍼스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.trainings[0].description").value("Python과 데이터 시각화"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.trainings[0].techStacks[0]").value("CSS"));
        // === 기타사항 ===
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.etcs[0].id").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.etcs[0].startDate")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.etcs[0].endDate")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.etcs[0].hasEndDate").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.etcs[0].title").value("오픈소스 컨트리뷰션"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.etcs[0].etcType").value("OTHER"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.etcs[0].institutionName").value("OSS Korea"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.resume.etcs[0].description").value("3건 PR 기여"));

        // 이력서 유저 정보
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.name").value("코스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.email").value("cos@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.birthDate").value(1999));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.resumeDTO.contactNumber").value("010-2345-6789"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
