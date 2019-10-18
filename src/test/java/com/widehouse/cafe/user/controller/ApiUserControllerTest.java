package com.widehouse.cafe.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.user.entity.User;
import com.widehouse.cafe.user.service.UserService;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 15..
 */
@WebMvcTest(ApiUserController.class)
@EnableSpringDataWebSupport
class ApiUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getCafesByMember() throws Exception {
        User user = new User(1L, "tester", "password");
        given(userService.getCafesByUser(user, PageRequest.of(0, 10, Sort.by(DESC, "cafe.createDateTime"))))
                .willReturn(Arrays.asList(new Cafe("url1", "name1"), new Cafe("url2", "name2"),
                        new Cafe("url3", "name3"), new Cafe("url4", "name4")));
        // then
        this.mockMvc.perform(get("/api/members/my/cafes")
                            .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.[0].url").value("url1"))
                .andExpect(jsonPath("$.[1].url").value("url2"))
                .andExpect(jsonPath("$.[2].url").value("url3"))
                .andExpect(jsonPath("$.[3].url").value("url4"));
    }

    @Test
    void getCafesByMemberWithPaging() throws Exception {
        User user = new User(1L, "tester", "password");
        given(this.userService.getCafesByUser(user,
                PageRequest.of(0, 3, Sort.by(DESC, "cafe.createDateTime"))))
                .willReturn(Arrays.asList(new Cafe("url1", "name1"), new Cafe("url2", "name2"),
                        new Cafe("url3", "name3")));
        // then
        this.mockMvc.perform(get("/api/members/my/cafes?page=0&size=3&sort=cafe.createDateTime,desc")
                            .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].url").value("url1"))
                .andExpect(jsonPath("$.[1].url").value("url2"))
                .andExpect(jsonPath("$.[2].url").value("url3"));
    }
}