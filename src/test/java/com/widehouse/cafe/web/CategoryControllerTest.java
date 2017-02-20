package com.widehouse.cafe.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.cafe.CategoryRepository;
import com.widehouse.cafe.service.CafeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
@EnableSpringDataWebSupport
public class CategoryControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private CafeService cafeService;

    @Test
    public void getCategories() throws Exception {
        // given
        given(categoryRepository.findAll())
                .willReturn(Arrays.asList(
                        new Category(1L, "Games"),
                        new Category(2L, "Comics"),
                        new Category(3L, "Broadcasts")
                ));
        mvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void getCafesByCategory() throws Exception {
        // given
        Category category1 = new Category("category");
        given(this.cafeService.getCafeByCategory(1L,
                new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount"))))
                .willReturn(Arrays.asList(
                        new Cafe("test1", "test1", "", CafeVisibility.PUBLIC, category1),
                        new Cafe("test2", "test2", "", CafeVisibility.PUBLIC, category1)));
        // then
        this.mvc.perform(get("/categories/1/cafes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].url").value("test1"))
                .andExpect(jsonPath("$.[1].url").value("test2"));
    }

    @Test
    public void getCafesByCategoryWithPaging() throws Exception {
        // given
        Category category1 = new Category("category");
        given(this.cafeService.getCafeByCategory(1L,
                new PageRequest(0, 4, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount"))))
                .willReturn(Arrays.asList(
                        new Cafe("test1", "test1", "", CafeVisibility.PUBLIC, category1),
                        new Cafe("test2", "test2", "", CafeVisibility.PUBLIC, category1),
                        new Cafe("test3", "test3", "", CafeVisibility.PUBLIC, category1),
                        new Cafe("test4", "test4", "", CafeVisibility.PUBLIC, category1)));
        // then
        this.mvc.perform(get("/categories/1/cafes?page=0&size=4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.[0].url").value("test1"))
                .andExpect(jsonPath("$.[1].url").value("test2"))
                .andExpect(jsonPath("$.[2].url").value("test3"))
                .andExpect(jsonPath("$.[3].url").value("test4"));
    }
}