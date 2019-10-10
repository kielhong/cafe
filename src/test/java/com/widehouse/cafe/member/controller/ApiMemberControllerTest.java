package com.widehouse.cafe.member.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.member.entity.Member;
import com.widehouse.cafe.member.service.MemberService;

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
@WebMvcTest(ApiMemberController.class)
@EnableSpringDataWebSupport
class ApiMemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    void getCafesByMember() throws Exception {
        Member member = new Member(1L, "tester", "password", "nickname", "foo@bar.com");
        given(memberService.getCafesByMember(member, PageRequest.of(0, 10, new Sort(DESC, "cafe.createDateTime"))))
                .willReturn(Arrays.asList(new Cafe("url1", "name1"), new Cafe("url2", "name2"),
                        new Cafe("url3", "name3"), new Cafe("url4", "name4")));
        // then
        this.mockMvc.perform(get("/api/members/my/cafes")
                            .with(user(member)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.[0].url").value("url1"))
                .andExpect(jsonPath("$.[1].url").value("url2"))
                .andExpect(jsonPath("$.[2].url").value("url3"))
                .andExpect(jsonPath("$.[3].url").value("url4"));
    }

    @Test
    void getCafesByMemberWithPaging() throws Exception {
        Member member = new Member(1L, "tester", "password", "nickname", "foo@bar.com");
        given(this.memberService.getCafesByMember(member,
                PageRequest.of(0, 3, new Sort(Sort.Direction.DESC, "cafe.createDateTime"))))
                .willReturn(Arrays.asList(new Cafe("url1", "name1"), new Cafe("url2", "name2"),
                        new Cafe("url3", "name3")));
        // then
        this.mockMvc.perform(get("/api/members/my/cafes?page=0&size=3&sort=cafe.createDateTime,desc")
                            .with(user(member)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].url").value("url1"))
                .andExpect(jsonPath("$.[1].url").value("url2"))
                .andExpect(jsonPath("$.[2].url").value("url3"));
    }
}