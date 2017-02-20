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
    private Board board1;
    private Board board2;
    private Member writer;

    private Article article1;
    private Article article2;
    private Article article3;
    private Article article4;

    @Before
    public void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        board1 = new Board(cafe, "board1");
        board2 = new Board(cafe, "board2");
        writer = new Member("writer");

        Article article1 = new Article(board1, writer, "test article1", "test1");
        Article article2 = new Article(board1, writer, "test article2", "test2");
        Article article3 = new Article(board2, writer, "test article3", "test3");
        Article article4 = new Article(board2, writer, "test article3", "test3");
    }

    @Test
    public void getArticlesByCafe_Should_Return_ListArticleInCafeWithIdOrderDesc() {
        // given
        given(articleRepository.findByCafe(cafe, new PageRequest(0, 3, new Sort(DESC, "id"))))
                .willReturn(Arrays.asList(article3, article2, article1));
        // when
        List<Article> articles = articleService.getArticlesByCafe(cafe, 0, 3);
        // then
        then(articles)
                .containsExactly(article3, article2, article1);
    }

    @Test
    public void getArticlesByBoard_Should_Return_ListArticleInBoardWithIdOrderDesc() {
        // given
        given(articleRepository.findByBoard(board1, new PageRequest(0, 3, new Sort(DESC, "id"))))
                .willReturn(Arrays.asList(article2, article1));
        // when
        List<Article> articles = articleService.getArticlesByBoard(board1, 0, 3);
        // then
        then(articles)
                .containsExactly(article2, article1);
    }
}
