package com.example.leapit.integre;

import com.example.leapit.MyRestDoc;
import com.example.leapit._core.util.JwtUtil;
import com.example.leapit.board.like.LikeRequest;
import com.example.leapit.common.enums.Role;
import com.example.leapit.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class LikeControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mvc;

    private String accessToken;

    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        System.out.println("setUp");
        User ssar = User.builder()
                .id(1)
                .username("ssar")
                .role(Role.valueOf("PERSONAL"))
                .build();
        accessToken = JwtUtil.create(ssar);
    }

    // 좋아요 등록
    @Test
    public void save_test() throws Exception {
        // given
        LikeRequest.SaveDTO reqDTO = new LikeRequest.SaveDTO();
        reqDTO.setBoardId(3);

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/personal/like")
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
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.likeId").value(5));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.likeCount").value(3));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }


    // 좋아요 취소
    @Test
    public void delete_test() throws Exception {
        // given
        Integer id = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/personal/like/{id}", id)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.likeCount").value(0));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
