package com.widehouse.cafe.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        List<Board> boards = cafeService.listBoard(cafe);
        model.addAttribute("cafe", cafe);
        model.addAttribute("boards", boards);

        List<Map<String, String>> specialBoards = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("name", "카페태그보기");
        map1.put("type", "tag");
        specialBoards.add(map1);
        Map<String, String> map2 = new HashMap<>();
        map2.put("name", "베스트게시물");
        map2.put("type", "list");
        specialBoards.add(map2);
        Map<String, String> map3 = new HashMap<>();
        map3.put("name", "카페 캘린더");
        map3.put("type", "calendar");
        specialBoards.add(map3);
        Map<String, String> map4 = new HashMap<>();
        map4.put("name", "카페북 책꽂이");
        map4.put("type", "book");
        specialBoards.add(map4);
        model.addAttribute("specialBoards", specialBoards);

        return "cafe";
    }
}
