package com.example.leapit.integre;


import com.example.leapit.MyRestDoc;
import com.example.leapit._core.util.JwtUtil;
import com.example.leapit.common.enums.Role;
import com.example.leapit.user.User;
import com.example.leapit.user.UserRequest;
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


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    private String personalAccessToken;

    private String companyAccessToken;

    @BeforeEach
    public void setUp() {
        User ssar = User.builder().id(1).username("ssar").role(Role.valueOf("PERSONAL")).build();
        personalAccessToken = JwtUtil.create(ssar);

        User company01 = User.builder().id(6).username("company01").role(Role.valueOf("COMPANY")).build();
        companyAccessToken = JwtUtil.create(company01);
    }

    @AfterEach
    public void tearDown(){
        // 테스트 후 정리할 코드
        System.out.println("tearDown");
    }

    // 1. 개인 회원가입
    @Test
    public void personal_join_test() throws Exception {
        // given
        UserRequest.PersonalJoinDTO reqDTO = new UserRequest.PersonalJoinDTO();
        reqDTO.setName("가나다");
        reqDTO.setUsername("admin123");
        reqDTO.setPassword("Qw12!@34");
        reqDTO.setEmail("admin@example.com");
        reqDTO.setContactNumber("010-9999-9999");
        reqDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        reqDTO.setRole(Role.PERSONAL);


        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/personal/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("admin123"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("admin@example.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.role").value(Role.PERSONAL.name()));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+$")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }


    // 2. 기업 회원가입
    @Test
    public void company_join_test() throws Exception {
        // given
        UserRequest.CompanyJoinDTO reqDTO = new UserRequest.CompanyJoinDTO();
        reqDTO.setUsername("company06");
        reqDTO.setPassword("Qw12!@34");
        reqDTO.setEmail("company@example.com");
        reqDTO.setContactNumber("010-1234-5678");
        reqDTO.setRole(Role.COMPANY);


        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/company/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(9));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("company06"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("company@example.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.role").value(Role.COMPANY.name()));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+$")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 3. 유저네임 중복체크
    @Test
    public void check_username_available_test() throws Exception {
        // given
        String username = "ssar";

        String requestBody = om.writeValueAsString(username);
//        System.out.println(requestBody);

//         when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/check-username-available/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.available").value(false));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 4. 로그인
    @Test
    public void login_test() throws Exception {
        // given
        UserRequest.LoginDTO reqDTO = new UserRequest.LoginDTO();
        reqDTO.setUsername("ssar");
        reqDTO.setPassword("1234");
        reqDTO.setRole(Role.PERSONAL);
        reqDTO.setRememberMe(null);


        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

//         when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.accessToken")
                .value(Matchers.matchesPattern("^[^.]+\\.[^.]+\\.[^.]+$")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 5. 개인 회원정보 수정 화면
    @Test
    public void get_personal_update_form_test() throws Exception {
        // given
        Integer userId = 1;


        String requestBody = om.writeValueAsString(userId);
//        System.out.println(requestBody);

//         when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/personal/user/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + personalAccessToken)
        );
        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("ssar@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.role").value("PERSONAL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value("쌀"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.birthDate").value("2000-01-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.contactNumber").value("010-1234-5678"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    // 6. 기업 회원정보 수정 화면
    @Test
    public void get_company_update_form_test() throws Exception {
        // given
        Integer userId = 6;

        String requestBody = om.writeValueAsString(userId);
//        System.out.println(requestBody);

//         when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/company/user/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + companyAccessToken)
        );
        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("company01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("company01@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.role").value("COMPANY"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value(Matchers.nullValue()));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.birthDate").value(Matchers.nullValue()));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.contactNumber").value("02-1234-5678"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 7. 기업 회원정보 수정
    @Test
    public void company_update_test() throws Exception {
        // given
        UserRequest.CompanyUpdateDTO reqDTO = new UserRequest.CompanyUpdateDTO();
        reqDTO.setNewPassword("Qw12!@34");
        reqDTO.setConfirmPassword("Qw12!@34");
        reqDTO.setContactNumber("010-9876-5432");
        reqDTO.setEmail("company01@nate.com");


        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

//         when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/company/user")
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
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("company01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("company01@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.role").value("COMPANY"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value(Matchers.nullValue()));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.birthDate").value(Matchers.nullValue()));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.contactNumber").value("010-9876-5432"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 8. 개인 회원정보 수정
    @Test
    public void personal_update_test() throws Exception {
        // given
        UserRequest.PersonalUpdateDTO reqDTO = new UserRequest.PersonalUpdateDTO();
        reqDTO.setName("홍길동");
        reqDTO.setNewPassword("Abcd1234!");
        reqDTO.setConfirmPassword("Abcd1234!");
        reqDTO.setEmail("hong.gildong@example.com");
        reqDTO.setContactNumber("010-1234-5678");


        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

//         when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/personal/user")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + personalAccessToken)
        );
        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("hong.gildong@example.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.role").value("PERSONAL"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value("홍길동"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.birthDate").value("2000-01-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.contactNumber").value("010-1234-5678"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
