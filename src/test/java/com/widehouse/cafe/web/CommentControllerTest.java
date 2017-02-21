package com.widehouse.cafe.web;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

/**
 * Created by kiel on 2017. 2. 20..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CommentService commentService;

    @Test
    public void getComments_Should_ListComments() throws Exception {
        // given
        Cafe cafe = new Cafe("testurl", "testcafe", "", PUBLIC, new Category("category"));
        Board board = new Board(cafe, "board");
        Member member = new Member("member");
        Article article = new Article(board, member, "title", "content");
        Comment comment1 = new Comment(article, member, "comment1");
        Comment comment2 = new Comment(article, member, "comment2");
        Comment comment3 = new Comment(article, member, "comment3");
        Comment comment4 = new Comment(article, member, "comment4");
        Comment comment5 = new Comment(article, member, "comment5");
        given(commentService.getComments(any(Member.class), eq(1L), eq(0), eq(5)))
                .willReturn(Arrays.asList(comment1, comment2, comment3, comment4, comment5));
        // then
        mvc.perform(get("/articles/1/comments?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }
}