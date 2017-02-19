package com.widehouse.cafe.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.cafe.Board;
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
    MockMvc mvc;
    @MockBean
    ArticleService articleService;
    @MockBean
    CafeRepository cafeRepository;

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
                        new Article(cafe, board, writer, "test article1", "test1"),
                        new Article(cafe, board, writer, "test article2", "test2"),
                        new Article(cafe, board, writer, "test article3", "test3"))
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
}
