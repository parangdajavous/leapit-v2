package com.example.leapit.integre;

import com.example.leapit.MyRestDoc;
import com.example.leapit._core.util.JwtUtil;
import com.example.leapit.common.enums.CareerLevel;
import com.example.leapit.common.enums.EducationLevel;
import com.example.leapit.common.enums.Role;
import com.example.leapit.jobposting.JobPostingRequest;
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

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class JobPostingControllerTest extends MyRestDoc {

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
    public void save_test() throws Exception {
        // given
        JobPostingRequest.SaveDTO reqDTO = new JobPostingRequest.SaveDTO();
        reqDTO.setTitle("백엔드 개발자 모집");
        reqDTO.setPositionType("백엔드");
        reqDTO.setMinCareerLevel(CareerLevel.valueOf("YEAR_2"));
        reqDTO.setMaxCareerLevel(CareerLevel.valueOf("YEAR_5"));
        reqDTO.setEducationLevel(EducationLevel.valueOf("BACHELOR"));
        reqDTO.setAddressRegionId(1);
        reqDTO.setAddressSubRegionId(1);
        reqDTO.setAddressDetail("123");
        reqDTO.setServiceIntro("우리는 AI 기반 솔루션을 제공합니다.");
        reqDTO.setDeadline(LocalDate.parse("2025-06-30"));
        reqDTO.setResponsibility("API 개발 및 운영");
        reqDTO.setQualification("Spring Boot 숙련자");
        reqDTO.setPreference("AWS 경험자");
        reqDTO.setBenefit("점심 제공, 자율 출퇴근");
        reqDTO.setAdditionalInfo("포트폴리오 제출 필수");
        reqDTO.setTechStackCodes(List.of("SPRING", "JAVA", "AWS"));

        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.post("/s/api/company/jobposting")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + companyAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(13));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("백엔드 개발자 모집"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positionType").value("백엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.minCareerLevel").value("2년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.maxCareerLevel").value("5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.educationLevel").value("학사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.addressRegionId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.addressSubRegionId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.addressDetail").value("123"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.serviceIntro").value("우리는 AI 기반 솔루션을 제공합니다."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.deadline").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.responsibility").value("API 개발 및 운영"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.qualification").value("Spring Boot 숙련자"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.preference").value("AWS 경험자"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("점심 제공, 자율 출퇴근"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.additionalInfo").value("포트폴리오 제출 필수"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.viewCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt", matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[0]").value("SPRING"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[1]").value("JAVA"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[2]").value("AWS"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void get_save_form_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/company/jobposting/new")
                        .header("Authorization", "Bearer " + companyAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positionTypes[0]").value("AI 엔지니어"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positionTypes[1]").value("데이터 엔지니어"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positionTypes[2]").value("모바일 앱 개발자"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positionTypes[3]").value("백엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positionTypes[4]").value("풀스택"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positionTypes[5]").value("프론트엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[0]").value("CSS"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[1]").value("Django"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[2]").value("HTML"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[3]").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[4]").value("Kotlin"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[5]").value("Node.js"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[6]").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[7]").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[8]").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[9]").value("Spring Boot"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].name").value("서울특별시"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[0].name").value("강남구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[1].name").value("서초구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].name").value("부산광역시"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].subRegions[0].name").value("부산진구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].subRegions[1].name").value("해운대구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[0]").value("YEAR_0"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[1]").value("YEAR_1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[2]").value("YEAR_2"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[3]").value("YEAR_3"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[4]").value("YEAR_4"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[5]").value("YEAR_5"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[6]").value("YEAR_6"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[7]").value("YEAR_7"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[8]").value("YEAR_8"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[9]").value("YEAR_9"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[10]").value("OVER_10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.educationLevels[0]").value("NO_PREFERENCE"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.educationLevels[1]").value("HIGH_SCHOOL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.educationLevels[2]").value("ASSOCIATE"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.educationLevels[3]").value("BACHELOR"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.educationLevels[4]").value("MASTER"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.educationLevels[5]").value("DOCTOR"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    @Test
    public void delete_test() throws Exception {
        // given
        Integer jobPostingId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/company/jobposting/{id}", jobPostingId)
                        .header("Authorization", "Bearer " + companyAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body").isEmpty());
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void get_update_form_test() throws Exception {
        // given
        Integer jobPostingId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/company/jobposting/{id}/edit", jobPostingId)
                        .header("Authorization", "Bearer " + companyAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positionTypes[0]").value("AI 엔지니어"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStackCodes[0]").value("CSS"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].name").value("서울특별시"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[0].name").value("강남구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[0]").value("신입"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.educationLevels[0]").value("무관"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.title").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.positionType").value("백엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.minCareerLevel").value("5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.maxCareerLevel").value("10년차 이상"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.educationLevel").value("고등학교"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.addressRegionId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.addressSubRegionId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.addressDetail").value("강남대로 123"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.serviceIntro").value("대용량 트래픽 처리 기반 백엔드 플랫폼 개발"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.deadline").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.responsibility").value("마이크로서비스 아키텍처 기반 시스템 설계 및 운영"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.qualification").value("Java, Spring 기반 개발 경험 필수"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.preference").value("AWS 경험자 우대"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.benefit").value("탄력 근무제, 점심 제공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.techStackCodes[0]").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.techStackCodes[1]").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.detailDTO.techStackCodes[2]").value("React"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void update_test() throws Exception {
        // given
        Integer jobPostingId = 1;

        JobPostingRequest.UpdateDTO reqDTO = new JobPostingRequest.UpdateDTO();
        reqDTO.setTitle("시니어 백엔드 개발자 수정");
        reqDTO.setPositionType("백엔드");
        reqDTO.setMinCareerLevel(CareerLevel.valueOf("YEAR_5"));
        reqDTO.setMaxCareerLevel(CareerLevel.valueOf("OVER_10"));
        reqDTO.setEducationLevel(EducationLevel.valueOf("HIGH_SCHOOL"));
        reqDTO.setAddressRegionId(1);
        reqDTO.setAddressSubRegionId(1);
        reqDTO.setAddressDetail("강남대로 456");
        reqDTO.setServiceIntro("수정된 서비스 소개");
        reqDTO.setDeadline(LocalDate.parse("2025-07-15"));
        reqDTO.setResponsibility("수정된 업무 내용");
        reqDTO.setQualification("Spring 숙련자");
        reqDTO.setPreference("AWS 경험자 우대");
        reqDTO.setBenefit("점심 제공, 유연 출퇴근");
        reqDTO.setAdditionalInfo("추가 서류 필요");
        reqDTO.setTechStackCodes(List.of("Java", "Spring Boot", "Docker"));

        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/company/jobposting/{id}", jobPostingId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + companyAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(jobPostingId));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("시니어 백엔드 개발자 수정"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positionType").value("백엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.minCareerLevel").value("5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.maxCareerLevel").value("10년차 이상"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.educationLevel").value("고등학교"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.addressRegionId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.addressSubRegionId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.addressDetail").value("강남대로 456"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.serviceIntro").value("수정된 서비스 소개"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.deadline").value("2025-07-15"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.responsibility").value("수정된 업무 내용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.qualification").value("Spring 숙련자"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.preference").value("AWS 경험자 우대"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("점심 제공, 유연 출퇴근"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.additionalInfo").value("추가 서류 필요"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.viewCount").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt", matchesPattern("\\d{4}-\\d{2}-\\d{2}.*")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[0]").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[1]").value("Spring Boot"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[2]").value("Docker"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void company_get_detail_form_test() throws Exception {
        // given
        Integer jobPostingId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/company/jobposting/{id}/detail", jobPostingId)
                        .header("Authorization", "Bearer " + companyAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positionType").value("백엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.minCareerLevel").value("5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.maxCareerLevel").value("10년차 이상"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.educationLevel").value("고등학교"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.addressRegionId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.addressSubRegionId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.addressDetail").value("강남대로 123"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.serviceIntro").value("대용량 트래픽 처리 기반 백엔드 플랫폼 개발"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.deadline").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.responsibility").value("마이크로서비스 아키텍처 기반 시스템 설계 및 운영"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.qualification").value("Java, Spring 기반 개발 경험 필수"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.preference").value("AWS 경험자 우대"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("탄력 근무제, 점심 제공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.additionalInfo").doesNotExist());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.viewCount").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt", matchesPattern("\\d{4}-\\d{2}-\\d{2}.*")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[0]").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[1]").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[2]").value("React"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void personal_get_detail_form_test() throws Exception {
        // given
        Integer jobPostingId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/personal/jobposting/{id}/detail", jobPostingId)
                        .header("Authorization", "Bearer " + personalAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.title").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.positionType").value("백엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.minCareerLevel").value("5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.maxCareerLevel").value("10년차 이상"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.educationLevel").value("고등학교"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.addressRegionId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.addressSubRegionId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.addressDetail").value("강남대로 123"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.serviceIntro").value("대용량 트래픽 처리 기반 백엔드 플랫폼 개발"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.deadline").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.responsibility").value("마이크로서비스 아키텍처 기반 시스템 설계 및 운영"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.qualification").value("Java, Spring 기반 개발 경험 필수"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.preference").value("AWS 경험자 우대"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.benefit").value("탄력 근무제, 점심 제공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.additionalInfo").doesNotExist());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.viewCount").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.createdAt", matchesPattern("\\d{4}-\\d{2}-\\d{2}.*")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.techStacks[0]").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.techStacks[1]").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyDTO.techStacks[2]").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyInfo.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyInfo.logoImage").value("점핏주식회사로고이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyInfo.companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyInfo.establishmentDate").value("2017-07-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyInfo.mainService").value("https://www.google.co.kr/"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void get_company_list_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/company/jobposting")
                        .header("Authorization", "Bearer " + companyAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[0].jobPostingId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[0].title").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[0].deadLine").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[0].open").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[1].jobPostingId").value(7));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[1].title").value("프론트엔드 웹 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[1].deadLine").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[1].open").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[2].jobPostingId").value(8));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[2].title").value("모바일 프론트엔드 앱 개발자"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[2].deadLine").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openPostings[2].open").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.closedPostings").isEmpty());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.openCount").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.closedCount").value(0));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }


    @Test
    public void get_list_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/jobposting")
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[0]").value("AI 엔지니어"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[1]").value("데이터 엔지니어"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[2]").value("모바일 앱 개발자"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[3]").value("백엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[4]").value("풀스택"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[5]").value("프론트엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[0]").value("CSS"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[1]").value("Django"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[2]").value("HTML"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[3]").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[4]").value("Kotlin"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[5]").value("Node.js"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[6]").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[7]").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[8]").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[9]").value("Spring Boot"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[0]").value("YEAR_0"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[1]").value("YEAR_1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[2]").value("YEAR_2"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[3]").value("YEAR_3"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[4]").value("YEAR_4"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[5]").value("YEAR_5"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[6]").value("YEAR_6"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[7]").value("YEAR_7"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[8]").value("YEAR_8"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[9]").value("YEAR_9"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[10]").value("OVER_10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].name").value("서울특별시"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[0].name").value("강남구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[1].id").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[1].name").value("서초구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].id").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].name").value("부산광역시"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].subRegions[0].id").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].subRegions[0].name").value("부산진구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].subRegions[1].id").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].subRegions[1].name").value("해운대구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].title").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].image").value("점핏주식회사대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].career").value("5년차 ~ 10년차 이상"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].techStacks[0].name").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].techStacks[1].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].techStacks[2].name").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].id").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].title").value("데이터 엔지니어 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].address").value("서울시 마포구 백범로 12길 22"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].image").value("랩핏테크대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].career").value("신입 ~ 2년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].techStacks[0].name").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].techStacks[1].name").value("Node.js"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].techStacks[2].name").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].id").value(7));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].title").value("프론트엔드 웹 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].image").value("점핏주식회사대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].career").value("2년차 ~ 5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].techStacks[0].name").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].techStacks[1].name").value("HTML"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].techStacks[2].name").value("CSS"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].id").value(8));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].title").value("모바일 프론트엔드 앱 개발자"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].image").value("점핏주식회사대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].career").value("2년차 ~ 5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].techStacks[0].name").value("Node.js"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].techStacks[1].name").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].techStacks[2].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].id").value(9));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].title").value("iOS 앱 개발자 구인"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].address").value("서울시 마포구 백범로 12길 22"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].image").value("랩핏테크대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].career").value("1년차 ~ 4년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].techStacks[0].name").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].techStacks[1].name").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].techStacks[2].name").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].id").value(10));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].title").value("AI 연구원 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].companyName").value("코드몽키"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].address").value("경기도 성남시 분당구 판교로 235"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].image").value("코드몽키대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].career").value("신입 ~ 2년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].techStacks[0].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].techStacks[1].name").value("Spring Boot"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].techStacks[2].name").value("HTML"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].id").value(11));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].title").value("풀스택 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].companyName").value("코드몽키"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].address").value("경기도 성남시 분당구 판교로 235"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].image").value("코드몽키대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].career").value("4년차 ~ 8년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].techStacks[0].name").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].techStacks[1].name").value("Django"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].techStacks[2].name").value("CSS"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].id").value(12));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].title").value("QA 엔지니어 모집"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].address").value("서울시 마포구 백범로 12길 22"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].image").value("랩핏테크대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].career").value("1년차 ~ 5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].techStacks[0].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].techStacks[1].name").value("Kotlin"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].techStacks[2].name").value("Spring Boot"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void get_personal_list_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/personal/jobposting")
                        .header("Authorization", "Bearer " + personalAccessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[0]").value("AI 엔지니어"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[1]").value("데이터 엔지니어"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[2]").value("모바일 앱 개발자"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[3]").value("백엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[4]").value("풀스택"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.positions[5]").value("프론트엔드"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[0]").value("CSS"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[1]").value("Django"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[2]").value("HTML"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[3]").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[4]").value("Kotlin"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[5]").value("Node.js"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[6]").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[7]").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[8]").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.techStacks[9]").value("Spring Boot"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[0]").value("YEAR_0"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[1]").value("YEAR_1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[2]").value("YEAR_2"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[3]").value("YEAR_3"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[4]").value("YEAR_4"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[5]").value("YEAR_5"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[6]").value("YEAR_6"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[7]").value("YEAR_7"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[8]").value("YEAR_8"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[9]").value("YEAR_9"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.careerLevels[10]").value("OVER_10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].name").value("서울특별시"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[0].name").value("강남구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[1].id").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[0].subRegions[1].name").value("서초구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].id").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].name").value("부산광역시"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].subRegions[0].id").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].subRegions[0].name").value("부산진구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].subRegions[1].id").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.regions[1].subRegions[1].name").value("해운대구"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].title").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].image").value("점핏주식회사대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].career").value("5년차 ~ 10년차 이상"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].bookmarked").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].techStacks[0].name").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].techStacks[1].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[0].techStacks[2].name").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].id").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].title").value("데이터 엔지니어 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].address").value("서울시 마포구 백범로 12길 22"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].image").value("랩핏테크대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].career").value("신입 ~ 2년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].bookmarked").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].techStacks[0].name").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].techStacks[1].name").value("Node.js"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[1].techStacks[2].name").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].id").value(7));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].title").value("프론트엔드 웹 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].image").value("점핏주식회사대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].career").value("2년차 ~ 5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].techStacks[0].name").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].techStacks[1].name").value("HTML"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[2].techStacks[2].name").value("CSS"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].id").value(8));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].title").value("모바일 프론트엔드 앱 개발자"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].image").value("점핏주식회사대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].career").value("2년차 ~ 5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].techStacks[0].name").value("Node.js"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].techStacks[1].name").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[3].techStacks[2].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].id").value(9));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].title").value("iOS 앱 개발자 구인"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].address").value("서울시 마포구 백범로 12길 22"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].image").value("랩핏테크대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].career").value("1년차 ~ 4년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].techStacks[0].name").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].techStacks[1].name").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[4].techStacks[2].name").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].id").value(10));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].title").value("AI 연구원 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].companyName").value("코드몽키"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].address").value("경기도 성남시 분당구 판교로 235"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].image").value("코드몽키대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].career").value("신입 ~ 2년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].techStacks[0].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].techStacks[1].name").value("Spring Boot"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[5].techStacks[2].name").value("HTML"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].id").value(11));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].title").value("풀스택 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].companyName").value("코드몽키"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].address").value("경기도 성남시 분당구 판교로 235"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].image").value("코드몽키대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].career").value("4년차 ~ 8년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].techStacks[0].name").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].techStacks[1].name").value("Django"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[6].techStacks[2].name").value("CSS"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].id").value(12));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].title").value("QA 엔지니어 모집"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].address").value("서울시 마포구 백범로 12길 22"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].image").value("랩핏테크대표이미지.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].career").value("1년차 ~ 5년차"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].deadline")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].bookmarked").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].techStacks[0].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].techStacks[1].name").value("Kotlin"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingList[7].techStacks[2].name").value("Spring Boot"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }


    // 구직자 - 메인페이지 (로그인)
    @Test
    public void index_login_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/")
                        .header("Authorization", "Bearer " + personalAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

        // 최신공고 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].title").value("QA 엔지니어 모집"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].active").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].bookmarkStatus").value("NOT_BOOKMARKED"));

        // 인기공고 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].title").value("데이터 엔지니어 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].image").value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].address").value(matchesPattern("서울특별시 서초구")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].career").value(matchesPattern("(신입, )?경력 \\d+-\\d+년")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].viewCount").value(13));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].techStacks[0].name").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].techStacks[1].name").value("Node.js"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].techStacks[2].name").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].bookmarkStatus").value("BOOKMARKED"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 구직자 - 메인페이지 (로그아웃)
    @Test
    public void index_logout_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

        // 최신공고 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].title").value("QA 엔지니어 모집"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].active").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].bookmarkStatus").value("NOT_BOOKMARKED"));

        // 인기공고 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].title").value("데이터 엔지니어 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].companyName").value("랩핏테크"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.recent[0].image").value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].address").value(matchesPattern("서울특별시 서초구")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].career").value(matchesPattern("(신입, )?경력 \\d+-\\d+년")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].dday").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].viewCount").value(13));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].techStacks[0].name").value("SQL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].techStacks[1].name").value("Node.js"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].techStacks[2].name").value("React"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.popular[0].bookmarkStatus").value("NOT_BOOKMARKED"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

}
