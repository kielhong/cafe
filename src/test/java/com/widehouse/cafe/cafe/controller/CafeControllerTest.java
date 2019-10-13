package com.widehouse.cafe.cafe.controller;

import static com.widehouse.cafe.article.entity.BoardType.CALENDAR;
import static com.widehouse.cafe.article.entity.BoardType.TAG;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.service.CafeService;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 24..
 */
@WebMvcTest(CafeController.class)
class CafeControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CafeService cafeService;

    @Test
    void getCafe_thenCafeInfo() throws Exception {
        // given
        Cafe cafe = new Cafe("cafetest", "cafename");
        Board board1 = Board.builder().cafe(cafe).name("board1").listOrder(1).build();
        Board board2 = Board.builder().cafe(cafe).name("board2").listOrder(2).build();
        Board board3 = Board.builder().cafe(cafe).name("board3").type(TAG).listOrder(3).build();
        Board board4 = Board.builder().cafe(cafe).name("board4").type(CALENDAR).listOrder(4).build();

        List<Board> boards = Arrays.asList(board1, board2, board3, board4);
        given(cafeService.getCafe("cafetest"))
                .willReturn(cafe);
        given(cafeService.listBoard(cafe))
                .willReturn(boards);
        // then
        mvc.perform(get("/cafes/cafetest"))
                .andExpect(status().isOk())
                .andExpect(view().name("cafe"))
                .andExpect(model().attribute("cafe", cafe))
                .andExpect(model().attribute("boards", Arrays.asList(board1, board2)))
                .andExpect(model().attribute("specialBoards", Arrays.asList(board3, board4)));
    }
}