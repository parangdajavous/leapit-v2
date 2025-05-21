package com.example.leapit.integre;

import com.example.leapit.MyRestDoc;
import com.example.leapit._core.util.JwtUtil;
import com.example.leapit.board.BoardRequest;
import com.example.leapit.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.matchesPattern;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;


    private String accessToken;

    @BeforeEach
    public void setUp(){
        // 테스트 시작 전에 실행할 코드
        User ssar = User.builder().id(1).username("ssar").build();
        accessToken = JwtUtil.create(ssar);
    }

    // 게시글 등록
    @Test
    public void save_test() throws Exception {
        // given
        BoardRequest.SaveDTO reqDTO = new BoardRequest.SaveDTO();
        reqDTO.setTitle("제목8");
        reqDTO.setContent("내용8");

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/personal/board")
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
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(8));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제목8"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내용8"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value("쌀"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAtFormatted").value("2025.05.21"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    // 게시글 수정
    @Test
    public void update_test() throws Exception {
        // given
        Integer id = 1;
        BoardRequest.UpdateDTO reqDTO = new BoardRequest.UpdateDTO();
        reqDTO.setTitle("제1");
        reqDTO.setContent("내1");

        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/personal/board/{id}", id)
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
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value("쌀"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAtFormatted").value("2025.05.21"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }

    // 게시글 보기
    @Test
    public void get_one_test() throws Exception {
        // given
        Integer id = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/personal/board/{id}", id)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("취업 준비중인데 조언 부탁드립니다"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("개발자로 취업 준비 중입니다. 혹시 면접에서 자주 나오는 질문이나, 포트폴리오에 꼭 들어가야 하는 내용 있을까요?"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value("쌀"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAtFormatted").value("2025.05.21"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 게시글 목록
    @Test
    public void get_list_test() throws Exception {

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/personal/board")
                        .header("Authorization", "Bearer " + accessToken)

        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.[0].id").value(7));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.[0].title").value("펌펌테크... 다시는 가고 싶지 않네요"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.[0].name").value("러브"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.[0].isBoardOwner").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.[0].createdAtFormatted").value("2025.05.21"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.[0].createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 게시글 상세보기
    @Test
    public void get_detail_test() throws Exception {
        // given
        Integer id = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/personal/board/{id}/detail", id)
                        .header("Authorization", "Bearer " + accessToken)

        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("취업 준비중인데 조언 부탁드립니다"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("개발자로 취업 준비 중입니다. 혹시 면접에서 자주 나오는 질문이나, 포트폴리오에 꼭 들어가야 하는 내용 있을까요?"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value("쌀"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isBoardOwner").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAtFormatted").value("2025.05.21"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isLike").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.likeCount").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.likeId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replies[0].id").value(6));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replies[0].content").value("재밌게 잘 읽었습니다."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replies[0].name").value("러브"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replies[0].isReplyOwner").value(false));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 게시글 삭제
    @Test
    public void delete_test() throws Exception {
        // given
        Integer id = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/personal/board/{id}", id)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body").doesNotExist()); // body는 null 반환됨
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
