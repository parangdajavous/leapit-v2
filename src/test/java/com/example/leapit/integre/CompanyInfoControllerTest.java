package com.example.leapit.integre;

import com.example.leapit.MyRestDoc;
import com.example.leapit._core.util.JwtUtil;
import com.example.leapit.companyinfo.CompanyInfoRequest;
import com.example.leapit.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.hamcrest.Matchers.matchesPattern;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CompanyInfoControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mvc;


    private String logo = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAEklEQVR42mP8z/C/HwAFAgH+zViQowAAAABJRU5ErkJggg==";
    private String img = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/5+hHgAHggJ/PcVIMAAAAABJRU5ErkJggg==";


    // 기업정보 등록
    @Test
    public void save_test() throws Exception {
        // given
        User company04 = User.builder().id(4).username("company04").build();
        String accessToken = JwtUtil.create(company04);


        CompanyInfoRequest.SaveDTO reqDTO = new CompanyInfoRequest.SaveDTO();
        reqDTO.setLogoImageFileContent(logo);
        reqDTO.setCompanyName("Leapit");
        reqDTO.setEstablishmentDate(LocalDate.of(2000, 10, 15));
        reqDTO.setAddress("경기 성남시 분당구 불정로 6 그린팩토리 6-10층");
        reqDTO.setMainService("https://www.naver.com");
        reqDTO.setIntroduction("IT 서비스");
        reqDTO.setImageFileContent(img);
        reqDTO.setBenefit("복리후생");


        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/company/info")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.logoImage")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyName").value("Leapit"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.establishmentDate")
                .value("2000-10-15"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.formattedEstablishmentInfo")
                .value(matchesPattern("\\d{1,2}년차 \\(\\d{4}년 \\d{1,2}월 설립\\)")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.address").value("경기 성남시 분당구 불정로 6 그린팩토리 6-10층"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.mainService").value("https://www.naver.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.introduction").value("IT 서비스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("복리후생"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }

    // 기업정보 수정
    @Test
    public void update_test() throws Exception {
        // given
        User company01 = User.builder().id(6).username("company01").build();
        String accessToken = JwtUtil.create(company01);

        Integer id = 1;

        CompanyInfoRequest.UpdateDTO reqDTO = new CompanyInfoRequest.UpdateDTO();
        reqDTO.setLogoImageFileContent(logo);
        reqDTO.setCompanyName("랩핏");
        reqDTO.setEstablishmentDate(LocalDate.of(2000, 10, 15));
        reqDTO.setAddress("경기 성남시 분당구 불정로 6 그린팩토리");
        reqDTO.setMainService("https://www.naver.com");
        reqDTO.setIntroduction("IT 서비스");
        reqDTO.setImageFileContent(img);
        reqDTO.setBenefit("복리후생, 식대 지원");

        String requestBody = om.writeValueAsString(reqDTO);
        //System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/company/info/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.logoImage")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyName").value("랩핏"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.establishmentDate")
                .value("2000-10-15"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.formattedEstablishmentInfo")
                .value(matchesPattern("\\d{1,2}년차 \\(\\d{4}년 \\d{1,2}월 설립\\)")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.address").value("경기 성남시 분당구 불정로 6 그린팩토리"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.mainService").value("https://www.naver.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.introduction").value("IT 서비스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("복리후생, 식대 지원"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 기업정보 보기
    @Test
    public void get_one_test() throws Exception {
        // given
        User company01 = User.builder().id(6).username("company01").build();
        String accessToken = JwtUtil.create(company01);

        Integer id = 1;


        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/company/companyinfo/{id}", id)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.logoImage")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.establishmentDate")
                .value("2017-07-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.formattedEstablishmentInfo")
                .value(matchesPattern("\\d{1,2}년차 \\(\\d{4}년 \\d{1,2}월 설립\\)")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.mainService").value("https://www.google.co.kr/"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.introduction").value("우리는 혁신적인 구직 플랫폼입니다."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("유연근무제, 점심 제공, 워케이션 제도"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }


    // 기업정보 상세보기
    @Test
    public void get_detail_company_test() throws Exception {
        // given
        User company01 = User.builder().id(6).username("company01").build();
        String accessToken = JwtUtil.create(company01);

        Integer id = 1;


        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/company/companyinfo/{id}/detail", id)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.logoImage")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.establishmentDate")
                .value("2017-07-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.formattedEstablishmentInfo")
                .value(matchesPattern("\\d{1,2}년차 \\(\\d{4}년 \\d{1,2}월 설립\\)")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.mainService").value("https://www.google.co.kr/"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.introduction").value("우리는 혁신적인 구직 플랫폼입니다."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("유연근무제, 점심 제공, 워케이션 제도"));
        // 채용공고 수
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingCount").value(3));
        // 마감일 늦은 순 상위 공고
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].title").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].deadline").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].ddayLabel")
                .value(matchesPattern("D-\\d+")));
        // 공고의 기술스택
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[0].name").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[1].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[2].name").value("React"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }

    // 기업정보 상세보기 - 구직자
    @Test
    public void get_detail_personal_test() throws Exception {
        // given
        Integer id = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/personal/companyinfo/{id}/detail", id)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.logoImage")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.establishmentDate")
                .value("2017-07-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.formattedEstablishmentInfo")
                .value(matchesPattern("\\d{1,2}년차 \\(\\d{4}년 \\d{1,2}월 설립\\)")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.mainService").value("https://www.google.co.kr/"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.introduction").value("우리는 혁신적인 구직 플랫폼입니다."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("유연근무제, 점심 제공, 워케이션 제도"));
        // 채용공고 수
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingCount").value(3));
        // 마감일 늦은 순 상위 공고
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].title").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].deadline").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].ddayLabel")
                .value(matchesPattern("D-\\d+")));
        // 공고의 기술스택
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[0].name").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[1].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[2].name").value("React"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }


}
