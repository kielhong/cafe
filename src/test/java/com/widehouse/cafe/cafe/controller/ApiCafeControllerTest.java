package com.widehouse.cafe.cafe.controller;

import static com.widehouse.cafe.cafe.entity.CafeVisibility.PUBLIC;
import static java.time.LocalDateTime.now;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.cafe.service.CafeService;
import com.widehouse.cafe.common.exception.CafeNotFoundException;
import com.widehouse.cafe.user.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 18..
 */
@WebMvcTest(ApiCafeController.class)
class ApiCafeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CafeService cafeService;

    private Category category;

    @BeforeEach
    void setup() {
        category = new Category(1, "category", 1, now());
    }

    @Test
    void getCafeByUrl_thenReturnCafeInfo() throws Exception {
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
                .andExpect(jsonPath("$.data.memberCount").value(0))
                .andExpect(jsonPath("$.data.articleCount").value(0));
    }

    @Test
    void getCafeByUrl_withNotExistCafe_then404NotFound() throws Exception {
        // given
        given(cafeService.getCafe("cafeurl"))
                .willThrow(new CafeNotFoundException());
        // then
        this.mvc.perform(get("/api/cafes/cafeurl"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCafe_WithLoginMember_Should_CreateCafe() throws Exception {
        // given
        User member = new User(1L, "member", "password");
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
