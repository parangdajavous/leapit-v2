package com.example.leapit.integre;


import com.example.leapit.MyRestDoc;
import com.example.leapit.common.enums.Role;
import com.example.leapit.user.UserRequest;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Test
    public void company_join_test() throws Exception {
        // given
        UserRequest.PersonalJoinDTO reqDTO = new UserRequest.PersonalJoinDTO();
        reqDTO.setName("가나다");
        reqDTO.setUsername("admin123");
        reqDTO.setPassword("Qw12!@34");
        reqDTO.setEmail("admin@example.com");
        reqDTO.setContactNumber("010-9999-9999");
        reqDTO.setBirthDate(LocalDate.of(1990, 1, 1));
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

        // TODO : then 필요
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }


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
}
