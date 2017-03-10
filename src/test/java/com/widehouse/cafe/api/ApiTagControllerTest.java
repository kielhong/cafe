package com.widehouse.cafe.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

/**
 * Created by kiel on 2017. 3. 10..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ApiTagController.class, secure = false)
public class ApiTagControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CafeService cafeService;
    @MockBean
    private TagService tagService;

    @Test
    public void getTagsByCafe_Should_Success() throws Exception {
        // given
        Cafe cafe = new Cafe("testurl", "testcafe");
        given(cafeService.getCafe("testurl"))
                .willReturn(cafe);
        given(tagService.getTagsByCafe(cafe))
                .willReturn(Arrays.asList(new Tag("tag1"), new Tag("tag2")));
        // then
        mvc.perform(get("/api/cafes/testurl/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }
}
