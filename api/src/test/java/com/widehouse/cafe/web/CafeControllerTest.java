package com.widehouse.cafe.web;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.service.CafeService;
import org.junit.Test;
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
        Category category = new Category(1L, "category");
        Cafe cafe = new Cafe("cafeurl", "cafename", "", PUBLIC, category);
        cafe.getBoards().add(new Board(cafe, "board1", 1));
        cafe.getBoards().add(new Board(cafe, "board2", 2));
        given(cafeService.getCafe("cafeurl"))
                .willReturn(cafe);
        // then
        this.mvc.perform(get("/cafes/cafeurl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.url").value("cafeurl"))
                .andExpect(jsonPath("$.name").value("cafename"))
                .andExpect(jsonPath("$.visibility").value(PUBLIC.toString()))
                .andExpect(jsonPath("$.boards").isArray())
                .andExpect(jsonPath("$.category.id").value(category.getId()))
                .andExpect(jsonPath("$.category.name").value(category.getName()))
                .andExpect(jsonPath("$.statistics.cafeMemberCount").value(0))
                .andExpect(jsonPath("$.statistics.cafeArticleCount").value(0))
                .andExpect(jsonPath("$.boards.[0].name").value("board1"))
                .andExpect(jsonPath("$.boards.[1].name").value("board2"));
    }

    @Test
    public void getCafeById_Should_CafeInfo() throws Exception {
        // given
        Category category = new Category(1L, "category");
        Cafe cafe = new Cafe("cafeurl", "cafename", "", PUBLIC, category);
        cafe.getStatistics().increaseCafeMemberCount();
        given(cafeService.getCafe(1L))
                .willReturn(cafe);
        // then
        this.mvc.perform(get("/cafes?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.url").value("cafeurl"))
                .andExpect(jsonPath("$.name").value("cafename"))
                .andExpect(jsonPath("$.visibility").value(PUBLIC.toString()))
                .andExpect(jsonPath("$.boards").isArray())
                .andExpect(jsonPath("$.category.id").value(category.getId()))
                .andExpect(jsonPath("$.category.name").value(category.getName()))
                .andExpect(jsonPath("$.statistics.cafeMemberCount").value(1))
                .andExpect(jsonPath("$.statistics.cafeArticleCount").value(0));
    }
}
