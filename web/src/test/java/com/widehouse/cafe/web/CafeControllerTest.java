package com.widehouse.cafe.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by kiel on 2017. 2. 24..
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CafeController.class)
public class CafeControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void cafe_Should_CafeInfo() throws Exception {
        mvc.perform(get("/cafes/cafetest"))
                .andExpect(status().isOk())
                .andExpect(view().name("cafe"));
    }
}
