package com.example.leapit.integre;

import com.example.leapit.MyRestDoc;
import com.example.leapit._core.util.JwtUtil;
import com.example.leapit.common.enums.Role;
import com.example.leapit.companyinfo.CompanyInfo;
import com.example.leapit.companyinfo.CompanyInfoRepository;
import com.example.leapit.companyinfo.CompanyInfoRequest;
import com.example.leapit.jobposting.JobPosting;
import com.example.leapit.jobposting.JobPostingRepository;
import com.example.leapit.jobposting.techstack.JobPostingTechStack;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRepository;
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
import java.util.*;

import static org.hamcrest.Matchers.matchesPattern;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CompanyInfoControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyInfoRepository companyInfoRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;


    @Test
    public void save_test() throws Exception {
        // given
        User company04 = User.builder()
                .username("company04")
                .password("123456")
                .email("company04@gmail.com")
                .role(Role.COMPANY)
                .contactNumber("010-1234-5678")
                .build();

        User savedUser = userRepository.save(company04);

        String accessToken = JwtUtil.create(savedUser);

        CompanyInfoRequest.SaveDTO reqDTO = new CompanyInfoRequest.SaveDTO();
        reqDTO.setLogoImageFileContent("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAEklEQVR42mP8z/C/HwAFAgH+zViQowAAAABJRU5ErkJggg==");
        reqDTO.setCompanyName("Leapit");
        reqDTO.setEstablishmentDate(LocalDate.of(2000, 10, 15));
        reqDTO.setAddress("경기 성남시 분당구 불정로 6 그린팩토리 6-10층");
        reqDTO.setMainService("https://www.naver.com");
        reqDTO.setIntroduction("IT 서비스");
        reqDTO.setImageFileContent("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/5+hHgAHggJ/PcVIMAAAAABJRU5ErkJggg==");
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
        System.out.println(responseBody);

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
                .value("24년차 (2000년 10월 설립)"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.address").value("경기 성남시 분당구 불정로 6 그린팩토리 6-10층"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.mainService").value("https://www.naver.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.introduction").value("IT 서비스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("복리후생"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }

    @Test
    public void update_test() throws Exception {
        // given
        User user = userRepository.findByUsername("company01")
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        String token = JwtUtil.create(user);

        CompanyInfo companyInfo = companyInfoRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("company01의 회사 정보가 존재하지 않습니다"));

        CompanyInfoRequest.UpdateDTO reqDTO = new CompanyInfoRequest.UpdateDTO();
        reqDTO.setLogoImageFileContent("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAEklEQVR42mP8z/C/HwAFAgH+zViQowAAAABJRU5ErkJggg==");
        reqDTO.setCompanyName("카카오");
        reqDTO.setEstablishmentDate(LocalDate.of(2021, 3, 1));
        reqDTO.setAddress("서울특별시 송파구 올림픽로 300");
        reqDTO.setMainService("https://www.naver.com");
        reqDTO.setIntroduction("IT 서비스");
        reqDTO.setImageFileContent("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/5+hHgAHggJ/PcVIMAAAAABJRU5ErkJggg==");
        reqDTO.setBenefit("사내 카페, 식사 제공");

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/company/info/" + companyInfo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + token)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.logoImage")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyName").value("카카오"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.establishmentDate")
                .value("2021-03-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.formattedEstablishmentInfo").value("4년차 (2021년 3월 설립)"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.address").value("서울특별시 송파구 올림픽로 300"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.mainService").value("https://www.naver.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.introduction").value("IT 서비스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("사내 카페, 식사 제공"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void get_one_test() throws Exception {
        // given
        User user = userRepository.findByUsername("company01")
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        String token = JwtUtil.create(user);

        CompanyInfo companyInfo = companyInfoRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("company01의 회사 정보가 없습니다."));

        String requestBody = om.writeValueAsString(companyInfo);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/company/companyinfo/{id}", companyInfo.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.logoImage")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.establishmentDate")
                .value("2017-07-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.formattedEstablishmentInfo").value("7년차 (2017년 7월 설립)"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.mainService").value("https://www.google.co.kr/"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.introduction").value("우리는 혁신적인 구직 플랫폼입니다."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("유연근무제, 점심 제공, 워케이션 제도"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    @Test
    public void get_detail_company_test() throws Exception {
        // given
        User user = userRepository.findByUsername("company01")
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        String token = JwtUtil.create(user);

        CompanyInfo companyInfo = companyInfoRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("company01의 회사 정보 없음"));


        List<Object[]> results = jobPostingRepository.findByUserIdJoinJobPostingTechStacks(user.getId());

        Map<Integer, String> postingTitles = new LinkedHashMap<>();
        Map<Integer, List<String>> postingStacks = new HashMap<>();
        Map<Integer, LocalDate> postingDeadlines = new HashMap<>();

        for (Object[] row : results) {
            JobPosting jp = (JobPosting) row[0];
            JobPostingTechStack ts = (JobPostingTechStack) row[1];

            postingTitles.putIfAbsent(jp.getId(), jp.getTitle());
            postingDeadlines.putIfAbsent(jp.getId(), jp.getDeadline());

            if (ts != null) {
                postingStacks.computeIfAbsent(jp.getId(), k -> new ArrayList<>()).add(ts.getTechStack());
            }
        }

        Long jobPostingCount = jobPostingRepository.countByUserIdAndDeadlineAfter(user.getId());

        String requestBody = om.writeValueAsString(companyInfo);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/company/companyinfo/{id}/detail", companyInfo.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.logoImage")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.establishmentDate")
                .value("2017-07-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.formattedEstablishmentInfo").value("7년차 (2017년 7월 설립)"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.mainService").value("https://www.google.co.kr/"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.introduction").value("우리는 혁신적인 구직 플랫폼입니다."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("유연근무제, 점심 제공, 워케이션 제도"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingCount").value(jobPostingCount.intValue()));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings.length()").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].title").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].deadline").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].ddayLabel").value("D-40"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[0].name").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[1].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[2].name").value("React"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }

    @Test
    public void get_detail_personal_test() throws Exception {
        // given
        User user = userRepository.findByUsername("company01")
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        CompanyInfo companyInfo = companyInfoRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("company01의 회사 정보 없음"));

        List<Object[]> results = jobPostingRepository.findByUserIdJoinJobPostingTechStacks(user.getId());

        Map<Integer, String> postingTitles = new LinkedHashMap<>();
        Map<Integer, List<String>> postingStacks = new HashMap<>();
        Map<Integer, LocalDate> postingDeadlines = new HashMap<>();

        for (Object[] row : results) {
            JobPosting jp = (JobPosting) row[0];
            JobPostingTechStack ts = (JobPostingTechStack) row[1];

            postingTitles.putIfAbsent(jp.getId(), jp.getTitle());
            postingDeadlines.putIfAbsent(jp.getId(), jp.getDeadline());

            if (ts != null) {
                postingStacks.computeIfAbsent(jp.getId(), k -> new ArrayList<>()).add(ts.getTechStack());
            }
        }

        Long jobPostingCount = jobPostingRepository.countByUserIdAndDeadlineAfter(user.getId());

        String requestBody = om.writeValueAsString(companyInfo);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/personal/companyinfo/{id}/detail", companyInfo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.logoImage")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.companyName").value("점핏 주식회사"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.establishmentDate")
                .value("2017-07-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.formattedEstablishmentInfo").value("7년차 (2017년 7월 설립)"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.address").value("서울특별시 강남구 테헤란로 1길 10"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.mainService").value("https://www.google.co.kr/"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.introduction").value("우리는 혁신적인 구직 플랫폼입니다."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.image")
                .value(matchesPattern("^data:image\\/png;base64,.+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.benefit").value("유연근무제, 점심 제공, 워케이션 제도"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostingCount").value(jobPostingCount.intValue()));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings.length()").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].title").value("시니어 백엔드 개발자 채용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].deadline").value("2025-06-30"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].ddayLabel").value("D-40"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[0].name").value("Python"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[1].name").value("Java"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.jobPostings[0].techStacks[2].name").value("React"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }


}
