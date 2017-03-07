package com.widehouse.cafe.api;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.MemberDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by kiel on 2017. 3. 6..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ApiCafeCommandController.class)
public class ApiCafeCommandControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    private CafeService cafeService;
    @MockBean
    private MemberDetailsService memberDetailsService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void createCafe_WithLoginMember_Should_CreateCafe() throws Exception {
        // given
        Member member = new Member("member");
        given(memberDetailsService.getCurrentMember())
                .willReturn(member);
        given(cafeService.createCafe(member, "testurl", "testcafe", "desc", PUBLIC, 1L))
                .willReturn(new Cafe("testurl", "testcafe", "desc", PUBLIC, new Category(1L, "testcategory")));
        // then
        mvc.perform(post("/api/cafes")
                    .with(user(member))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("{\"name\": \"testcafe\", \"url\": \"testurl\", \"description\": \"desc\", \"visibility\": \"PUBLIC\", \"category\": {\"id\":\"1\"}}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.url").value("testurl"))
                .andExpect(jsonPath("$.name").value("testcafe"))
                .andExpect(jsonPath("$.visibility").value(PUBLIC.toString()));
    }
}
