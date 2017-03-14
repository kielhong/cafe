package com.widehouse.cafe.api;

import static com.widehouse.cafe.domain.cafe.BoardType.LIST;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import com.widehouse.cafe.projection.ArticleProjection;
import com.widehouse.cafe.service.ArticleService;
import com.widehouse.cafe.service.MemberDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

/**
 * Created by kiel on 2017. 2. 19..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ApiArticleController.class)
@Import(WebSecurityConfig.class)
@EnableSpringDataWebSupport
public class ApiArticleControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;
    @MockBean
    private CafeRepository cafeRepository;
    @MockBean
    private BoardRepository boardRepository;
    @MockBean
    private MemberDetailsService memberDetailsService;

    private Cafe cafe;
    private Board board;
    private Member writer;

    private ArticleProjection articleProjection1;
    private ArticleProjection articleProjection2;
    private ArticleProjection articleProjection3;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        cafe = new Cafe("testurl", "testcafe");
        board = new Board(1L, cafe, "board", LIST, 1);
        writer = new Member("writer");
    }

    @Test
    public void getArticlesByCafe_Should_ListArticles() throws Exception {
        // given
        given(cafeRepository.findByUrl(cafe.getUrl()))
                .willReturn(cafe);
        given(articleService.getArticlesByCafe(cafe, 0, 3))
                .willReturn(Arrays.asList(articleProjection3, articleProjection2, articleProjection1));
        // then
        mvc.perform(get("/api/cafes/" + cafe.getUrl() + "/articles?page=0&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void getArticlesByBoard_Should_ListArticles() throws Exception {
        // given
        given(cafeRepository.findByUrl(cafe.getUrl()))
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
        mvc.perform(get("/api/cafes/" + cafe.getUrl() + "/boards/" + board.getId() + "/articles?page=0&size=3"))
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

    @Test
    public void getArticle_Should_ReturnArticle() throws Exception {
        // given
        given(memberDetailsService.getCurrentMember())
                .willReturn(new Member("reader"));
        given(articleService.getArticle(anyLong(), any(Member.class)))
                .willReturn(new Article(new Board(new Cafe("testurl", "testname"), "board"), new Member("writer"), "title", "content"));
        // when
        mvc.perform(get("/api/cafes/testurl/articles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.board.name").value("board"));
    }

    @Test
    public void getArticle_WithNoAuthorityMember_Should_Error_403Forbidden() throws Exception {
        // given
        given(articleService.getArticle(anyLong(), any(Member.class)))
                .willThrow(new NoAuthorityException());
        // when
        mvc.perform(get("/api/cafes/testurl/articles/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void writeArticle_WithCafeMember_Should_Success() throws Exception {
        // given
        given(articleService.writeArticle(any(Board.class), any(Member.class), anyString(), anyString()))
                .willReturn(new Article(board, writer, "title", "content"));
        // then
        mvc.perform(post("/api/cafes/" + cafe.getUrl() + "/articles/")
                    .with(user(writer))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"test title\", \"content\":\"test content\", \"board\": {\"id\" : 1} }"))
                .andExpect(status().isOk());
    }
}
