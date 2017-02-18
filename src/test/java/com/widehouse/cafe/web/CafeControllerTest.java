package com.widehouse.cafe.web;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
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
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 18..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CafeController.class)
public class CafeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CafeService cafeService;

    @Test
    public void getCafeByUrl_Should_CafeInfo() throws Exception {
        // given
        CafeCategory category = new CafeCategory(1L, "category");
        given(cafeService.getCafe("cafeurl"))
                .willReturn(new Cafe("cafeurl", "cafename", "", PUBLIC, category));
        this.mvc.perform(get("/cafes/cafeurl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.url").value("cafeurl"))
                .andExpect(jsonPath("$.name").value("cafename"))
                .andExpect(jsonPath("$.visibility").value(PUBLIC.toString()))
                .andExpect(jsonPath("$.boards").isArray())
                .andExpect(jsonPath("$.category.id").value(category.getId()))
                .andExpect(jsonPath("$.category.name").value(category.getName()));
    }
}
