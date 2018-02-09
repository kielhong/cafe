package com.widehouse.cafe.api;

import static com.widehouse.cafe.domain.cafe.BoardType.LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.article.TagRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;
import com.widehouse.cafe.service.ArticleService;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.MemberDetailsService;
import com.widehouse.cafe.service.MemberService;
import com.widehouse.cafe.service.TagService;

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
 * Created by kiel on 2017. 3. 10..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ApiTagController.class)
@Import(WebSecurityConfig.class)
public class ApiTagControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CafeService cafeService;
    @MockBean
    private ArticleService articleService;
    @MockBean
    private TagService tagService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private MemberDetailsService memberDetailsService;

    private Cafe cafe;
    private Board board;
    private Member writer;

    @Before
    public void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        board = new Board(1L, cafe, "board", LIST, 1);
        writer = new Member("writer");

        given(cafeService.getCafe("testurl"))
                .willReturn(cafe);
    }

    @Test
    public void getTagsByCafe_thenListTags() throws Exception {
        given(tagService.getTagsByCafe(cafe))
                .willReturn(Arrays.asList(new Tag("tag1"), new Tag("tag2")));

        mvc.perform(get("/api/cafes/testurl/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void getArticlesByCafe_thenListArticles() throws Exception {
        // given
        Tag tag = new Tag("testtag");
        given(tagService.getTagByName("testtag"))
                .willReturn(tag);
        given(tagService.getArticlesByTag(cafe, tag))
                .willReturn(Arrays.asList(
                        new Article(board, writer, "title1", "content1"),
                        new Article(board, writer, "title2", "content2")));
        // then
        mvc.perform(get("/api/cafes/" + cafe.getUrl() + "/tags/" + tag.getName() + "/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void getTags_thenListTags() throws Exception {
        Article article = new Article(board, writer, "title", "content");
        article.getTags().add(new Tag("tag1"));
        article.getTags().add(new Tag("tag2"));
        given(articleService.getArticle(eq(1L), any()))
                .willReturn(article);
        // then
        mvc.perform(get("/api/articles/1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].name").value("tag1"))
                .andExpect(jsonPath("$.[1].name").value("tag2"));
    }

    @Test
    public void postTags_withArticleWriter_thenAddTags() throws Exception {
        // given
        Article article = new Article(board, writer, "article", "content");
        Tag tag1 = new Tag("testtag1");
        Tag tag2 = new Tag("testtag2");
        Tag tag3 = new Tag("testtag3");
        Tag tag4 = new Tag("testtag4");
        article.getTags().addAll(Arrays.asList(tag1, tag3, tag4));
        tag1.getArticles().add(article);
        tag3.getArticles().add(article);
        tag4.getArticles().add(article);
        given(articleService.getArticle(1L, writer))
                .willReturn(article);
        given(tagRepository.findByName("testtag1"))
                .willReturn(tag1);
        given(tagRepository.findByName("testtag3"))
                .willReturn(tag3);
        given(tagRepository.findByName("testtag4"))
                .willReturn(tag4);
        // then
        mvc.perform(post("/api/articles/1/tags")
                    .with(user(writer))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("[{\"name\":\"testtag1\"},{\"name\":\"testtag3\"},{\"name\":\"testtag4\"}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[?(@.name == 'testtag2')]").doesNotExist())  // not include
                .andExpect(jsonPath("$.[?(@.name == 'testtag3')]").exists())        // include
                .andExpect(jsonPath("$.[?(@.name == 'testtag4')]").exists());       // include
        verify(tagService).updateTagsOfArticle(eq(article), anyList());
    }
}
