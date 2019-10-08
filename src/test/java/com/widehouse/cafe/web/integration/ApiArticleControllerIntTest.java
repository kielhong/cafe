package com.widehouse.cafe.web.integration;

import static com.widehouse.cafe.domain.cafe.BoardType.LIST;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.ArticleService;
import com.widehouse.cafe.service.CafeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiArticleControllerIntTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CafeService cafeService;
    @Autowired
    private ArticleService articleService;

    private Cafe cafe;
    private Board board;
    private Member writer;

    @BeforeEach
    public void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        board = new Board(1L, cafe, "board", LIST, 1);
        writer = new Member(1L, "writer", "password", "nickname", "foo@bar.com");
    }

    @Test
    public void writeArticle_withCafeMember_thenSuccess() throws Exception {
        mvc.perform(post("/api/cafes/testurl/articles/")
                    .with(user(writer))
                    .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"test title\", \"content\":\"test content\", \"board\": {\"id\" : 1} }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.content").value("test content"))
                .andExpect(jsonPath("$.writer.id").value(writer.getId()))
                .andExpect(jsonPath("$.board.id").value(board.getId()));
    }
}
