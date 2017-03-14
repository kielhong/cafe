package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.service.CafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

/**
 * Created by kiel on 2017. 2. 24..
 */
@Controller
public class CafeController {
    @Autowired
    private CafeService cafeService;

    @GetMapping("/cafes/{url}")
    @Transactional
    public String cafe(@PathVariable String url, Model model) throws Exception {
        Cafe cafe = cafeService.getCafe(url);
        model.addAttribute("cafe", cafe);

        List<Board> boards = cafeService.listBoard(cafe);
        List<Board> specialBoards = boards.stream()
                .filter(b -> b.isSpecialType())
                .collect(Collectors.toList());
        model.addAttribute("specialBoards", specialBoards);

        List<Board> generalBoards = boards.stream()
                .filter(b -> !b.isSpecialType())
                .collect(Collectors.toList());
        model.addAttribute("boards", generalBoards);

        return "cafe";
    }
}
