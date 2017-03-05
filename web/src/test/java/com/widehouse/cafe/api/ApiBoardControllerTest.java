package com.widehouse.cafe.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 25..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ApiBoardController.class, secure = false)
public class ApiBoardControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BoardRepository boardRepository;

    @Test
    public void getBoard_Should_ReturnBoard() throws Exception {
        Cafe cafe = new Cafe("testurl", "testcafe");
        Board board = new Board(1L, cafe, "board", 1);
        given(boardRepository.findOne(1L))
                .willReturn(board);
        // then
        mvc.perform(get("/api/cafes/" + cafe.getUrl() + "/boards/" + board.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("board"));


    }
}
