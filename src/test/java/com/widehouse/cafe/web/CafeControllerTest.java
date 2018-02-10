package com.widehouse.cafe.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.widehouse.cafe.config.WebSecurityConfig;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.MemberDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 24..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CafeController.class)
@Import(WebSecurityConfig.class)
public class CafeControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CafeService cafeService;
    @MockBean
    private MemberDetailsService userDetailsService;

    @Test
    public void getCafe_Should_CafeInfo() throws Exception {
        // given
        Cafe cafe = new Cafe("cafetest", "cafename");
        List<Board> boards = Arrays.asList(new Board(cafe, "board1", 1), new Board(cafe, "board2", 2));
        given(cafeService.getCafe("cafetest"))
                .willReturn(cafe);
        given(cafeService.listBoard(cafe))
                .willReturn(boards);
        // then
        mvc.perform(get("/cafes/cafetest"))
                .andExpect(status().isOk())
                .andExpect(view().name("cafe"))
                .andExpect(model().attribute("cafe", cafe))
                .andExpect(model().attribute("boards", boards));
    }
}
