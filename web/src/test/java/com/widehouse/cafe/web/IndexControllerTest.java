package com.widehouse.cafe.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.service.CafeUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

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
    private CafeUserDetailsService userDetailsService;

    @Test
    public void index_Should_IndexPage() throws Exception {
        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }

    @Test
    public void login_Should_LoginFormPage() throws Exception {
        mvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("login"));
    }

    @Test
    public void loginSubmit_Should_AuthAndRedirectToIndex() throws Exception {
        // given
        given(userDetailsService.loadUserByUsername("user"))
                .willReturn(new User("user", "password", Arrays.asList(new SimpleGrantedAuthority("USER"))));
        // then
        mvc.perform(formLogin().user("user").password("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void logout_Should_RedirectToIndex() throws Exception {
        mvc.perform(logout())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }
}
