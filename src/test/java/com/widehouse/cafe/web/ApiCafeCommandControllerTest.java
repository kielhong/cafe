package com.widehouse.cafe.web;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.CafeService;

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
 * Created by kiel on 2017. 3. 6..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ApiCafeCommandController.class)
@Import(WebSecurityConfig.class)
public class ApiCafeCommandControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CafeService cafeService;

    @Test
    public void createCafe_WithLoginMember_Should_CreateCafe() throws Exception {
        // given
        Member member = new Member("member");
        given(cafeService.createCafe(member, "testurl", "testcafe", "desc", PUBLIC, 1L))
                .willReturn(new Cafe("testurl", "testcafe", "desc", PUBLIC, new Category(1L, "testcategory")));
        String requestContent = "{\"name\": \"testcafe\", \"url\": \"testurl\", \"description\": \"desc\", " +
                "\"visibility\": \"PUBLIC\", \"category\": {\"id\":\"1\"}}";

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
