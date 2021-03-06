package com.widehouse.cafe.article.controller;

import static com.widehouse.cafe.article.entity.BoardType.LIST;
import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.article.service.ArticleService;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.service.CafeService;
import com.widehouse.cafe.common.exception.NoAuthorityException;
import com.widehouse.cafe.user.entity.User;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Created by kiel on 2017. 2. 19..
 */
@WebMvcTest(ApiArticleController.class)
class ApiArticleControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;
    @MockBean
    private CafeService cafeService;
    @MockBean
    private ApplicationEventPublisher publisher;

    private Cafe cafe;
    private Board board;
    private User writer;

    @BeforeEach
    void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        board = new Board(1L, cafe, "board", LIST, 1);
        writer = new User(1L, "writer", "password");

        given(cafeService.getCafe("testurl"))
                .willReturn(cafe);
        given(cafeService.getBoard(1L))
                .willReturn(board);
    }

    @Test
    void listArticlesByCafe_thenListArticles() throws Exception {
        // given
        given(articleService.getArticles(cafe, 0, 3))
                .willReturn(Arrays.asList(
                        new Article(board, writer, "test article1", "test1"),
                        new Article(board, writer, "test article2", "test2"),
                        new Article(board, writer, "test article3", "test3")));
        // when
        ResultActions result = mvc.perform(get("/api/cafes/testurl/articles?page=0&size=3"));
        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void listArticlesByBoard_thenListArticles() throws Exception {
        given(articleService.getArticles(any(Board.class), anyInt(), anyInt()))
                .willReturn(Arrays.asList(
                        new Article(board, writer, "test article1", "test1"),
                        new Article(board, writer, "test article2", "test2"),
                        new Article(board, writer, "test article3", "test3")));
        // then
        mvc.perform(get("/api/cafes/testurl/boards/1/articles?page=0&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$..board[?(@.id == 1)]", hasSize(3)))
                .andExpect(jsonPath("$.[0].title").value("test article1"))
                .andExpect(jsonPath("$.[1].title").value("test article2"))
                .andExpect(jsonPath("$.[2].title").value("test article3"))
                .andExpect(jsonPath("$.[2].commentCount").value(0))
                .andExpect(jsonPath("$.[2].writer").exists());
    }

    @Test
    void getArticle_withAuh_thenReturnArticle() throws Exception {
        User reader = new User(1L, "reader", "password");
        given(articleService.readArticle(1L, reader))
                .willReturn(new Article(1L, board, writer, "title", "content", emptyList(), 0, 0, now(), now()));
        // when
        mvc.perform(get("/api/cafes/testurl/articles/1")
                    .with(user(reader)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.board.name").value("board"));
    }

    @Test
    void getArticle_withNoAuthorityMember_then403Forbidden() throws Exception {
        User reader = new User(1L, "reader", "password");
        given(articleService.readArticle(1L, reader))
                .willThrow(new NoAuthorityException());

        mvc.perform(get("/api/cafes/testurl/articles/1")
                    .with(user(reader)))
                .andExpect(status().isForbidden());
    }

    @Test
    void writeArticle_withCafeMember_thenSuccess() throws Exception {
        given(articleService.writeArticle(board, writer, "test title", "test content"))
                .willReturn(new Article(board, writer, "test title", "test content"));
        // then
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
