package com.widehouse.cafe.cafe.controller;

import static com.widehouse.cafe.cafe.service.CategoryService.ORDER;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.cafe.service.CategoryService;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 15..
 */
@WebMvcTest(ApiCategoryController.class)
class ApiCategoryControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CategoryService categoryService;

    @Test
    void getCategories() throws Exception {
        // given
        given(categoryService.findAll(ORDER))
                .willReturn(Arrays.asList(
                        new Category("Games", 1),
                        new Category("Comics", 2),
                        new Category("Broadcasts", 3)
                ));
        mvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].listOrder").value(1))
                .andExpect(jsonPath("$.[1].listOrder").value(2))
                .andExpect(jsonPath("$.[2].listOrder").value(3));
    }

    @Test
    void getCafesByCategory() throws Exception {
        // given
        given(this.categoryService.getCafeByCategory(1,
                PageRequest.of(0, 10, Sort.by(DESC, "statistics.cafeMemberCount"))))
                .willReturn(Arrays.asList(new Cafe("cafe1", "cafe1"), new Cafe("cafe2", "cafe2")));
        // then
        this.mvc.perform(get("/api/categories/1/cafes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getCafesByCategoryWithPaging() throws Exception {
        // given
        given(this.categoryService.getCafeByCategory(1,
                PageRequest.of(0, 4, Sort.by(DESC, "statistics.cafeMemberCount"))))
                .willReturn(Arrays.asList(new Cafe("cafe1", "cafe1"), new Cafe("cafe2", "cafe2"),
                        new Cafe("cafe3", "cafe3"), new Cafe("cafe4", "cafe4")));
        // then
        this.mvc.perform(get("/api/categories/1/cafes?page=0&size=4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(4));
    }
}