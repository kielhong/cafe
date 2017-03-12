package com.widehouse.cafe.api;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleTag;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;
import com.widehouse.cafe.projection.ArticleProjection;
import com.widehouse.cafe.service.ArticleService;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.MemberService;
import com.widehouse.cafe.service.TagService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 3. 10..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ApiTagController.class)
@Import(WebSecurityConfig.class)
public class ApiTagControllerTest {
    @Autowired
    private WebApplicationContext context;

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

    private Cafe cafe;
    private Board board;
    private Member writer;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        cafe = new Cafe("testurl", "testcafe");
        board = new Board(1L, cafe, "board", 1);
        writer = new Member("writer");
    }

    @Test
    public void getTagsByCafe_Should_Success() throws Exception {
        // given
        Cafe cafe = new Cafe("testurl", "testcafe");
        given(cafeService.getCafe("testurl"))
                .willReturn(cafe);
        given(tagService.getTagsByCafe(cafe))
                .willReturn(Arrays.asList(new Tag("tag1"), new Tag("tag2")));
        // then
        mvc.perform(get("/api/cafes/testurl/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void getArticlesByCafe_Should_Success() throws Exception {
        // given
        Tag tag = new Tag("testtag");
        given(cafeService.getCafe("testurl"))
                .willReturn(cafe);
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
    public void getTags_Should_ListTags() throws Exception {
        // given
        Cafe cafe = new Cafe("testurl", "testcafe");
        Board board = new Board(1L, cafe, "board", 1);
        Member writer = new Member("writer");
        Article article = new Article(board, writer, "title", "content");
        article.getArticleTags().add(new ArticleTag(article, new Tag("tag1")));
        article.getArticleTags().add(new ArticleTag(article, new Tag("tag2")));
        given(articleService.getArticle(eq(1L), any(Member.class)))
                .willReturn(article);
        // then
        mvc.perform(get("/api/articles/1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].name").value("tag1"))
                .andExpect(jsonPath("$.[1].name").value("tag2"));
    }

    @Test
    public void postTags_WhenArticleWriter_Should_Success() throws Exception {
        // given
        Article article = new Article(board, writer, "article", "content");
        Tag tag1 = new Tag("testtag1");
        Tag tag2 = new Tag("testtag2");
        Tag tag3 = new Tag("testtag3");
        Tag tag4 = new Tag("testtag4");
        ArticleTag at1 = new ArticleTag(article, tag1);
        ArticleTag at2 = new ArticleTag(article, tag2);
        ArticleTag at3 = new ArticleTag(article, tag3);
        ArticleTag at4 = new ArticleTag(article, tag4);
        article.getArticleTags().add(at1);
        article.getArticleTags().add(at3);
        article.getArticleTags().add(at4);
        tag1.getArticleTags().add(at1);
        tag3.getArticleTags().add(at3);
        tag4.getArticleTags().add(at4);
        given(articleService.getArticle(1L, writer))
                .willReturn(article);
        // then
        mvc.perform(post("/api/articles/1/tags")
                    .with(user(writer))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("[{\"name\":\"testtag1\"},{\"name\":\"testtag3\"},{\"name\":\"testtag4\"}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[?(@.name == 'testtag2')]").doesNotExist())  // not include
                .andExpect(jsonPath("$.[?(@.name == 'testtag3')]").exists())        // include
                .andExpect(jsonPath("$.[?(@.name == 'testtag4')]").exists());       // include
        verify(tagService).updateTagsOfArticle(eq(article), anyListOf(Tag.class));
    }
}
