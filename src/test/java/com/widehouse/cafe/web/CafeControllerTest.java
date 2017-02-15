package com.widehouse.cafe.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeCategory;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.service.CafeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CafeController.class)
public class CafeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CafeService cafeService;

    @Test
    public void getCafesByCategory() throws Exception {
        // given
        CafeCategory category1 = new CafeCategory("category");
        given(this.cafeService.getCafeByCategory(1L))
                .willReturn(Arrays.asList(
                        new Cafe("test1", "test1", "", CafeVisibility.PUBLIC, category1),
                        new Cafe("test2", "test2", "", CafeVisibility.PUBLIC, category1)));
        // then
        this.mvc.perform(get("/cafes/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].url").value("test1"))
                .andExpect(jsonPath("$.[1].url").value("test2"));
    }
}
