package com.widehouse.cafe.web;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static java.time.LocalDateTime.now;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeNotFoundException;
import com.widehouse.cafe.service.CafeService;

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
 * Created by kiel on 2017. 2. 18..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ApiCafeController.class)
@Import(WebSecurityConfig.class)
public class ApiCafeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CafeService cafeService;

    private Category category;

    @Before
    public void setup() {
        category = new Category(1, "category", 1, now());
    }

    @Test
    public void getCafeByUrl_thenReturnCafeInfo() throws Exception {
        Cafe cafe = new Cafe("cafeurl", "cafename", "", PUBLIC, category);
        given(cafeService.getCafe("cafeurl"))
                .willReturn(cafe);
        // then
        this.mvc.perform(get("/api/cafes/cafeurl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.url").value("cafeurl"))
                .andExpect(jsonPath("$.name").value("cafename"))
                .andExpect(jsonPath("$.visibility").value(PUBLIC.toString()))
                .andExpect(jsonPath("$.category.id").value(category.getId()))
                .andExpect(jsonPath("$.category.name").value(category.getName()))
                .andExpect(jsonPath("$.statistics.cafeMemberCount").value(0))
                .andExpect(jsonPath("$.statistics.cafeArticleCount").value(0));
    }

    @Test
    public void getCafeByUrl_withNotExistCafe_then404NotFound() throws Exception {
        // given
        given(cafeService.getCafe("cafeurl"))
                .willThrow(new CafeNotFoundException());
        // then
        this.mvc.perform(get("/api/cafes/cafeurl"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createCafe_WithLoginMember_Should_CreateCafe() throws Exception {
        // given
        Member member = new Member(1L, "member", "password", "nickname", "foo@bar.com");
        given(cafeService.createCafe(member, "testurl", "testcafe", "desc", PUBLIC, 1))
                .willReturn(new Cafe("testurl", "testcafe", "desc", PUBLIC, category));
        String requestContent = "{\"name\": \"testcafe\", \"url\": \"testurl\", \"description\": \"desc\", "
                + "\"visibility\": \"PUBLIC\", \"category\": {\"id\":\"1\"}}";

        mvc.perform(post("/api/cafes")
                .with(user(member))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("testurl"))
                .andExpect(jsonPath("$.name").value("testcafe"))
                .andExpect(jsonPath("$.visibility").value(PUBLIC.toString()));
    }
}
