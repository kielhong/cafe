package com.widehouse.cafe.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by kiel on 2017. 2. 24..
 */
@Controller
public class CafeController {
//    @Autowired
//    private CafeService cafeService;
    @GetMapping("/cafes/{url}")
    public String cafe(@PathVariable String url, Model model) {
//        Cafe cafe = cafeService.getCafe(url);
//        model.addAttribute("cafe", cafe);

        return "cafe";
    }
}
