package com.widehouse.cafe.web;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static java.time.LocalDateTime.now;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.article.CommentRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.SimpleMember;
import com.widehouse.cafe.exception.NoAuthorityException;
import com.widehouse.cafe.service.CommentService;
import com.widehouse.cafe.service.MemberDetailsService;
import com.widehouse.cafe.web.ApiCommentController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 20..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ApiCommentController.class)
@Import(WebSecurityConfig.class)
@Slf4j
public class ApiCommentControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private MemberDetailsService memberDetailsService;

    private Article article;
    private Member member;

    @Before
    public void setUp() {
        member = new Member(1L, "member");

        Cafe cafe = new Cafe("testurl", "testcafe", "", PUBLIC, new Category("category", 1));
        Board board = new Board(cafe, "board");
        article = new Article(1L, board, member, "title", "content", new ArrayList<>(), 0, now(), now());

        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article));
    }

    @Test
    public void getComments_thenListComments() throws Exception {
        // given
        Comment comment1 = new Comment(article, member, "comment1");
        Comment comment2 = new Comment(article, member, "comment2");
        Comment comment3 = new Comment(article, member, "comment3");
        Comment comment4 = new Comment(article, member, "comment4");
        Comment comment5 = new Comment(article, member, "comment5");
        given(commentService.getComments(member, 1L, 0, 5))
                .willReturn(Arrays.asList(comment1, comment2, comment3, comment4, comment5));
        // then
        mvc.perform(get("/api/articles/1/comments?page=0&size=5")
                    .with(user(member)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    public void write_withCafeMember_thenCreateComment() throws Exception {
        given(commentService.writeComment(article, member, "new comment"))
                .willReturn(new Comment(article, member, "new comment"));

        mvc.perform(post("/api/articles/1/comments")
                    .with(user(member))
                    .with(csrf())
                    .contentType(APPLICATION_JSON)
                    .content("{\"comment\":\"new comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("new comment"))
                .andExpect(jsonPath("$.member.id").value(member.getId()));
    }

    @Test
    public void write_withNonCafeMember_then403Forbidden() throws Exception {
        // given
        Comment comment = new Comment(article, member, "new comment");
        given(commentService.writeComment(article, member, comment.getComment()))
                .willThrow(new NoAuthorityException());
        // then
        mvc.perform(post("/api/articles/1/comments")
                    .contentType(APPLICATION_JSON)
                    .content("{\"comment\":\"new comment\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void writeReplyComment_whenSubComment_thenSuccess() throws Exception {
        // given
        Comment comment = new Comment("1", article.getId(), new SimpleMember(member), "comment",
                Collections.emptyList(), now(), now());
        String subCommentText = "sub comment";
        given(commentRepository.findById("1"))
                .willReturn(Optional.of(comment));
        given(commentService.writeReplyComment(comment, member, subCommentText))
                .willReturn(new Comment(article.getId(), member, subCommentText));
        // then
        mvc.perform(post("/api/comments/1/comments")
                    .with(user(member))
                    .with(csrf())
                    .contentType(APPLICATION_JSON)
                    .content("{\"comment\":\"" + subCommentText + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value(subCommentText))
                .andExpect(jsonPath("$.articleId").value(article.getId()))
                .andExpect(jsonPath("$.member.id").value(member.getId()));
    }
}