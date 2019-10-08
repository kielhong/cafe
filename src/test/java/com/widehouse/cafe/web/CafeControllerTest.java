package com.widehouse.cafe.web;

import static com.widehouse.cafe.domain.cafe.BoardType.CALENDAR;
import static com.widehouse.cafe.domain.cafe.BoardType.TAG;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.service.CafeService;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 24..
 */
@WebMvcTest(CafeController.class)
@Import(WebSecurityConfig.class)
public class CafeControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CafeService cafeService;

    @Test
    public void getCafe_thenCafeInfo() throws Exception {
        // given
        Cafe cafe = new Cafe("cafetest", "cafename");
        Board board1 = new Board(cafe, "board1", 1);
        Board board2 = new Board(cafe, "board2", 2);
        Board board3 = new Board(cafe, "board3", TAG, 3);
        Board board4 = new Board(cafe, "board4", CALENDAR, 4);

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