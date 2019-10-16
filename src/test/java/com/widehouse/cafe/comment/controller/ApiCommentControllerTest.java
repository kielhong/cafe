package com.widehouse.cafe.comment.controller;

import static com.widehouse.cafe.cafe.entity.CafeVisibility.PUBLIC;
import static java.time.LocalDateTime.now;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.comment.entity.Comment;
import com.widehouse.cafe.comment.entity.CommentRepository;
import com.widehouse.cafe.comment.service.CommentService;
import com.widehouse.cafe.common.exception.NoAuthorityException;
import com.widehouse.cafe.user.entity.User;
import com.widehouse.cafe.user.entity.SimpleUser;
import com.widehouse.cafe.user.service.UserDetailsServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

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
    private CommentRepository commentRepository;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private Article article;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "member", "password");

        Cafe cafe = new Cafe("testurl", "testcafe", "", PUBLIC, new Category("category", 1));
        Board board = Board.builder().cafe(cafe).name("board").build();
        article = new Article(1L, board, user, "title", "content", new ArrayList<>(), 0, now(), now());

        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article));
    }

    @Test
    void getComments_thenListComments() throws Exception {
        // given
        Comment comment1 = new Comment(article, user, "comment1");
        Comment comment2 = new Comment(article, user, "comment2");
        Comment comment3 = new Comment(article, user, "comment3");
        Comment comment4 = new Comment(article, user, "comment4");
        Comment comment5 = new Comment(article, user, "comment5");
        given(commentService.getComments(user, 1L, 0, 5))
                .willReturn(Arrays.asList(comment1, comment2, comment3, comment4, comment5));
        // then
        mvc.perform(get("/api/articles/1/comments?page=0&size=5")
                    .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void write_withCafeMember_thenCreateComment() throws Exception {
        given(commentService.writeComment(article, user, "new comment"))
                .willReturn(new Comment(article, user, "new comment"));

        mvc.perform(post("/api/articles/1/comments")
                    .with(user(user))
                    .with(csrf())
                    .contentType(APPLICATION_JSON)
                    .content("{\"comment\":\"new comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("new comment"))
                .andExpect(jsonPath("$.member.id").value(user.getId()));
    }

    @Test
    void write_withNonCafeMember_then403Forbidden() throws Exception {
        // given
        Comment comment = new Comment(article, user, "new comment");
        given(commentService.writeComment(article, user, comment.getComment()))
                .willThrow(new NoAuthorityException());
        // then
        mvc.perform(post("/api/articles/1/comments")
                    .contentType(APPLICATION_JSON)
                    .content("{\"comment\":\"new comment\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void writeReplyComment_whenSubComment_thenSuccess() throws Exception {
        // given
        Comment comment = new Comment("1", 1L, article.getId(), new SimpleUser(user), "comment",
                Collections.emptyList(), now(), now());
        String subCommentText = "sub comment";
        given(commentRepository.findById("1"))
                .willReturn(Optional.of(comment));
        given(commentService.writeReplyComment(comment, user, subCommentText))
                .willReturn(new Comment(1L, article.getId(), user, subCommentText));
        // then
        mvc.perform(post("/api/comments/1/comments")
                    .with(user(user))
                    .with(csrf())
                    .contentType(APPLICATION_JSON)
                    .content("{\"comment\":\"" + subCommentText + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value(subCommentText))
                .andExpect(jsonPath("$.articleId").value(article.getId()))
                .andExpect(jsonPath("$.member.id").value(user.getId()));
    }
}