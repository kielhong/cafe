package com.widehouse.cafe.web;

import static com.widehouse.cafe.domain.cafe.BoardType.LIST;
import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import com.widehouse.cafe.service.ArticleService;
import com.widehouse.cafe.service.CafeService;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 19..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ApiArticleController.class)
@Import(WebSecurityConfig.class)
public class ApiArticleControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;
    @MockBean
    private CafeService cafeService;

    private Cafe cafe;
    private Board board;
    private Member writer;

    @Before
    public void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        board = new Board(1L, cafe, "board", LIST, 1);
        writer = new Member(1L, "writer");

        given(cafeService.getCafe("testurl"))
                .willReturn(cafe);
        given(cafeService.getBoard(1L))
                .willReturn(board);
    }

    @Test
    public void listArticlesByCafe_thenListArticles() throws Exception {
        given(articleService.getArticlesByCafe(cafe, 0, 3))
                .willReturn(Arrays.asList(
                        new Article(board, writer, "test article1", "test1"),
                        new Article(board, writer, "test article2", "test2"),
                        new Article(board, writer, "test article3", "test3")));
        // then
        mvc.perform(get("/api/cafes/testurl/articles?page=0&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void listArticlesByBoard_thenListArticles() throws Exception {
        given(articleService.getArticlesByBoard(board, 0, 3))
                .willReturn(Arrays.asList(
                        new Article(board, writer, "test article1", "test1"),
                        new Article(board, writer, "test article2", "test2"),
                        new Article(board, writer, "test article3", "test3")));
        // then
        mvc.perform(get("/api/cafes/testurl/boards/1/articles?page=0&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$..cafe[?(@.url == 'testurl')]", hasSize(3)))
                .andExpect(jsonPath("$..board[?(@.id == 1)]", hasSize(3)))
                .andExpect(jsonPath("$.[0].title").value("test article1"))
                .andExpect(jsonPath("$.[1].title").value("test article2"))
                .andExpect(jsonPath("$.[2].title").value("test article3"))
                .andExpect(jsonPath("$.[2].commentCount").value(0))
                .andExpect(jsonPath("$.[2].writer").exists());
    }

    @Test
    public void getArticle_withAuh_thenReturnArticle() throws Exception {
        Member reader = new Member("reader");
        given(articleService.getArticle(1L, reader))
                .willReturn(new Article(1L, board, writer, "title", "content", emptyList(), 0, now(), now()));
        // when
        mvc.perform(get("/api/cafes/testurl/articles/1")
                    .with(user(reader)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.board.name").value("board"));
    }

    @Test
    public void getArticle_withNoAuthorityMember_then403Forbidden() throws Exception {
        Member reader = new Member("reader");
        given(articleService.getArticle(1L, reader))
                .willThrow(new NoAuthorityException());

        mvc.perform(get("/api/cafes/testurl/articles/1")
                    .with(user(reader)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void writeArticle_withCafeMember_thenSuccess() throws Exception {
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
