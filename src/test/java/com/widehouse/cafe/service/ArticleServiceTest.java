package com.widehouse.cafe.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 19..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceTest {
    @MockBean
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;

    private Cafe cafe;
    private Board board;
    private Member writer;

    @Before
    public void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        board = new Board(cafe, "board");
        writer = new Member("writer");
    }

    @Test
    public void getArticlesByCafe_Should_Return_ListArticleWithIdOrderDesc() {
        // given
        Article article1 = new Article(cafe, board, writer, "test article1", "test1");
        Article article2 = new Article(cafe, board, writer, "test article2", "test2");
        Article article3 = new Article(cafe, board, writer, "test article3", "test3");
        given(articleRepository.findByCafe(cafe, new PageRequest(0, 3, new Sort(DESC, "id"))))
                .willReturn(Arrays.asList(article3, article2, article1));
        // when
        List<Article> articles = articleService.getArticlesByCafe(cafe, 0, 3);
        // then
        then(articles)
                .containsExactly(article3, article2, article1);

    }
}
