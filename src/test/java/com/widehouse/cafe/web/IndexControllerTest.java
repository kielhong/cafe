package com.widehouse.cafe.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafe.CategoryRepository;
import com.widehouse.cafe.domain.member.Member;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

/**
 * Created by kiel on 2017. 2. 23..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(IndexController.class)
@Import(WebSecurityConfig.class)
public class IndexControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsService memberDetailsService;
    @MockBean
    private PasswordEncoder encoder;
    @MockBean
    private CategoryRepository categoryRepository;

    private Member member;

    @Before
    public void setup() {
        member = new Member(1L, "user", "encodedPass", "foo@bar.com");
        given(memberDetailsService.loadUserByUsername("user"))
                .willReturn(member);
        given(encoder.matches("password", "encodedPass"))
                .willReturn(true);
    }

    @Test
    public void index_thenIndexPage() throws Exception {
        // given
        Category category1 = new Category("test1", 1);
        Category category2 = new Category("test2", 2);
        given(categoryRepository.findAll(any(Sort.class)))
                .willReturn(Arrays.asList(category1, category2));
        // then
        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attribute("categories", Arrays.asList(category1, category2)));
    }

    @Test
    public void login_thenLoginForm() throws Exception {
        mvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("login"));
    }

    @Test
    public void loginSubmit_thenAuthAndRedirectToIndex() throws Exception {
        mvc.perform(formLogin()
                    .user("user").password("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/")).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void logout_thenRedirectToIndex() throws Exception {
        mvc.perform(logout())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void createCafeForm_withLoginUser_thenCafeCreationForm() throws Exception {
        mvc.perform(get("/createCafe"))
            .andExpect(status().isOk())
            .andExpect(view().name("create_cafe"));
    }

    @Test
    public void createCafeForm_withNonLogin_thenRedirectLoginForm() throws Exception {
        mvc.perform(get("/createCafe"))
                .andExpect(status().is3xxRedirection());
        //.andExpect(redirectedUrl("/login"));
    }
}