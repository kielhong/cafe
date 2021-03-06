package com.widehouse.cafe.article.controller;

import static com.widehouse.cafe.article.entity.BoardType.LIST;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.article.service.ArticleService;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.service.CafeService;
import com.widehouse.cafe.user.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ApiArticleControllerIntTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CafeService cafeService;
    @Autowired
    private ArticleService articleService;

    private Cafe cafe;
    private Board board;
    private User writer;

    @BeforeEach
    void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        board = new Board(1L, cafe, "board", LIST, 1);
        writer = new User(1L, "writer", "password");
    }

    @Sql("classpath:data.sql")
    @Test
    void writeArticle_withCafeMember_thenSuccess() throws Exception {
        mvc.perform(post("/api/cafes/cafetest/articles/")
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
