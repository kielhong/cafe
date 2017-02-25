package com.widehouse.cafe.web;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafe.CategoryRepository;
import com.widehouse.cafe.projection.CafeProjection;
import com.widehouse.cafe.service.CafeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

    private CafeProjection cafeMock1;
    private CafeProjection cafeMock2;
    private CafeProjection cafeMock3;
    private CafeProjection cafeMock4;

    @Test
    public void getCategories() throws Exception {
        // given
        given(categoryRepository.findAll(new Sort(ASC, "listOrder")))
                .willReturn(Arrays.asList(
                        new Category("Games", 1),
                        new Category("Comics", 2),
                        new Category("Broadcasts", 3)
                ));
        mvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].listOrder").value(1))
                .andExpect(jsonPath("$.[1].listOrder").value(2))
                .andExpect(jsonPath("$.[2].listOrder").value(3));
    }

    @Test
    public void getCafesByCategory() throws Exception {
        // given
        given(this.cafeService.getCafeByCategory(1L,
                new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount"))))
                .willReturn(Arrays.asList(cafeMock1, cafeMock2));
        // then
        this.mvc.perform(get("/categories/1/cafes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void getCafesByCategoryWithPaging() throws Exception {
        // given
        given(this.cafeService.getCafeByCategory(1L,
                new PageRequest(0, 4, new Sort(DESC, "statistics.cafeMemberCount"))))
                .willReturn(Arrays.asList(cafeMock1, cafeMock2, cafeMock3, cafeMock4));
        // then
        this.mvc.perform(get("/categories/1/cafes?page=0&size=4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(4));
    }
}