package com.widehouse.cafe.home.controller;

import static java.time.LocalDateTime.now;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.cafe.service.CategoryService;
import com.widehouse.cafe.member.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

/**
 * Created by kiel on 2017. 2. 23..
 */
@WebMvcTest(IndexController.class)
class IndexControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsService memberDetailsService;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private PasswordEncoder encoder;

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(1L, "user", "encodedPass", "nickname", "foo@bar.com");
        given(memberDetailsService.loadUserByUsername("user"))
                .willReturn(member);
        given(encoder.matches("password", "encodedPass"))
                .willReturn(true);
    }

    @Test
    void index_thenIndexPage() throws Exception {
        List<Category> categories = new ArrayList<>();
        IntStream.range(1, 11)
                .forEach(i -> categories.add(new Category(i, "test" + i, i, now())));
        given(categoryService.findAll(CategoryService.ORDER))
                .willReturn(categories);
        // then
        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index.vue"))
            .andExpect(model().attribute("categories", categories));
    }

    @Test
    void login_thenLoginForm() throws Exception {
        mvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("login"));
    }

    @Test
    void loginSubmit_thenAuthAndRedirectToIndex() throws Exception {
        mvc.perform(formLogin()
                    .user("user").password("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void logout_thenRedirectToIndex() throws Exception {
        mvc.perform(logout())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser
    void createCafeForm_withLoginUser_thenCafeCreationForm() throws Exception {
        mvc.perform(get("/createCafe"))
            .andExpect(status().isOk())
            .andExpect(view().name("create_cafe"));
    }

    @Test
    void createCafeForm_withAnonymouseUser_thenRedirectLoginForm() throws Exception {
        mvc.perform(get("/createCafe"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}