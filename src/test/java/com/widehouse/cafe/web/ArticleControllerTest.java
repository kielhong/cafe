package com.widehouse.cafe.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

/**
 * Created by kiel on 2017. 2. 19..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ArticleService articleService;
    @MockBean
    private CafeRepository cafeRepository;
    @MockBean
    private BoardRepository boardRepository;

    @Test
    public void getArticlesByCafe_Should_ListArticles() throws Exception {
        Cafe cafe = new Cafe("testurl", "testcafe");
        Board board = new Board(cafe, "board");
        Member writer = new Member("writer");
        // given
        given(cafeRepository.findByUrl("testurl"))
                .willReturn(cafe);
        given(articleService.getArticlesByCafe(cafe, 0, 3))
                .willReturn(Arrays.asList(
                        new Article(board, writer, "test article1", "test1"),
                        new Article(board, writer, "test article2", "test2"),
                        new Article(board, writer, "test article3", "test3"))
                );
        // then
        mvc.perform(get("/cafes/" + cafe.getUrl() + "/articles?page=0&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].cafe.url").value(cafe.getUrl()))
                .andExpect(jsonPath("$.[0].title").value("test article1"))
                .andExpect(jsonPath("$.[1].cafe.url").value(cafe.getUrl()))
                .andExpect(jsonPath("$.[1].title").value("test article2"))
                .andExpect(jsonPath("$.[2].cafe.url").value(cafe.getUrl()))
                .andExpect(jsonPath("$.[2].title").value("test article3"))
                .andExpect(jsonPath("$.[2].commentCount").value(0))
                .andExpect(jsonPath("$.[2].writer").exists());
    }

    @Test
    public void getArticlesByBoard_Should_ListArticles() throws Exception {
        Cafe cafe = new Cafe("testurl", "testcafe");
        Board board = new Board(1L, cafe, "board", 1);
        Member writer = new Member("writer");
        // given
        given(cafeRepository.findByUrl("testurl"))
                .willReturn(cafe);
        given(boardRepository.findOne(board.getId()))
                .willReturn(board);
        given(articleService.getArticlesByBoard(board, 0, 3))
                .willReturn(Arrays.asList(
                        new Article(board, writer, "test article1", "test1"),
                        new Article(board, writer, "test article2", "test2"),
                        new Article(board, writer, "test article3", "test3"))
                );
        // then
        mvc.perform(get("/cafes/" + cafe.getUrl() + "/boards/" + board.getId() + "/articles?page=0&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].cafe.url").value(cafe.getUrl()))
                .andExpect(jsonPath("$.[0].board.id").value(board.getId()))
                .andExpect(jsonPath("$.[0].title").value("test article1"))
                .andExpect(jsonPath("$.[0].board.id").value(board.getId()))
                .andExpect(jsonPath("$.[1].title").value("test article2"))
                .andExpect(jsonPath("$.[0].board.id").value(board.getId()))
                .andExpect(jsonPath("$.[2].title").value("test article3"))
                .andExpect(jsonPath("$.[2].commentCount").value(0))
                .andExpect(jsonPath("$.[2].writer").exists());
    }
}
