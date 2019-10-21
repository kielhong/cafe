package com.widehouse.cafe.comment.controller;

import static com.widehouse.cafe.cafe.entity.CafeVisibility.PUBLIC;
import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.comment.entity.Comment;
import com.widehouse.cafe.comment.service.CommentListService;
import com.widehouse.cafe.comment.service.CommentService;
import com.widehouse.cafe.common.exception.NoAuthorityException;
import com.widehouse.cafe.user.entity.SimpleUser;
import com.widehouse.cafe.user.entity.User;
import com.widehouse.cafe.user.service.UserDetailsServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 20..
 */
@WebMvcTest(ApiCommentController.class)
class ApiCommentControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;
    @MockBean
    private CommentListService commentListService;
    @MockBean
    private ArticleService articleservice;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private Article article;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "member", "password");

        Cafe cafe = new Cafe("testurl", "testcafe", "", PUBLIC, new Category("category", 1));
        Board board = Board.builder().cafe(cafe).name("board").build();
        article = new Article(1L, board, user, "title", "content", new ArrayList<>(), 0, 0, now(), now());
    }

    @Test
    void getComments_thenListComments() throws Exception {
        // given
        given(articleservice.readArticle(anyLong(), any(User.class)))
                .willReturn(article);
        List<Comment> comments = IntStream.range(1, 6)
                .mapToObj(i -> new Comment(article, user, "comment" + i))
                .collect(Collectors.toList());
        given(commentListService.listComments(any(User.class), anyLong(), anyInt(), anyInt()))
                .willReturn(comments);
        // then
        this.mvc.perform(get("/api/articles/1/comments?page=0&size=5")
                    .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void write_withCafeMember_thenCreateComment() throws Exception {
        // given
        given(articleservice.readArticle(anyLong(), any(User.class)))
                .willReturn(article);
        given(commentService.writeComment(article, user, "new comment"))
                .willReturn(new Comment(article, user, "new comment"));
        // expected
        this.mvc.perform(post("/api/articles/1/comments")
                    .with(user(user))
                    .with(csrf())
                    .contentType(APPLICATION_JSON)
                    .content("{\"comment\":\"new comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("new comment"))
                .andExpect(jsonPath("$.member.id").value(user.getId()));
    }

    @Test
    void write_withNonCafeMember_then403Forbidden() throws Exception {
        // given
        Comment comment = new Comment(article, user, "new comment");
        given(commentService.writeComment(article, user, comment.getText()))
                .willThrow(new NoAuthorityException());
        // then
        mvc.perform(post("/api/articles/1/comments")
                    .contentType(APPLICATION_JSON)
                    .content("{\"text\":\"new comment\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void writeReplyComment_whenSubComment_thenSuccess() throws Exception {
        // given
        Comment comment = new Comment("1", 1L, article.getId(), new SimpleUser(user), "comment",
                Collections.emptyList(), now(), now());
        String subCommentText = "sub comment";
        given(commentService.getComment("1"))
                .willReturn(comment);
        given(commentService.writeReplyComment(comment, user, subCommentText))
                .willReturn(new Comment(1L, article.getId(), user, subCommentText));
        // then
        mvc.perform(post("/api/comments/1/comments")
                    .with(user(user))
                    .with(csrf())
                    .contentType(APPLICATION_JSON)
                    .content("{\"comment\":\"" + subCommentText + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(subCommentText))
                .andExpect(jsonPath("$.articleId").value(article.getId()))
                .andExpect(jsonPath("$.member.id").value(user.getId()));
    }
}