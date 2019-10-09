package com.widehouse.cafe.article.controller;

import static com.widehouse.cafe.article.entity.BoardType.LIST;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.article.controller.ApiBoardController;
import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.service.CafeService;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 25..
 */
@WebMvcTest(ApiBoardController.class)
@Import(WebSecurityConfig.class)
public class ApiBoardControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CafeService cafeService;

    private Cafe cafe;

    @BeforeEach
    public void setup() {
        cafe = new Cafe(1L, "testurl", "testcafe");

        given(cafeService.getCafe("testurl"))
                .willReturn(cafe);
    }

    @Test
    public void getBoard_thenReturnBoard() throws Exception {
        Board board = new Board(1L, cafe, "board", LIST, 1);
        given(cafeService.getBoard(1L))
                .willReturn(board);

        mvc.perform(get("/api/cafes/testurl/boards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("board"));
    }

    @Test
    public void getBoard_withOtherCafe_then404NotFound() throws Exception {
        Cafe otherCafe = new Cafe(2L, "otherturl", "othercafe");
        Board board = new Board(1L, cafe, "board", LIST, 1);
        given(cafeService.getCafe("otherurl"))
                .willReturn(otherCafe);
        given(cafeService.getBoard(1L))
                .willReturn(board);

        mvc.perform(get("/api/cafes/otherurl/boards/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBoardsByCafe_thenListBoardsInCafe() throws Exception {
        given(cafeService.listBoard(cafe))
                .willReturn(Arrays.asList(new Board(cafe, "board1", 1), new Board(cafe, "board2", 2)));

        mvc.perform(get("/api/cafes/testurl/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

}
