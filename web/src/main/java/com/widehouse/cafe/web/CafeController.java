package com.widehouse.cafe.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by kiel on 2017. 2. 24..
 */
@Controller
public class CafeController {
    @GetMapping("/cafes/{url}")
    public String cafe(@PathVariable String url) {
        return "cafe";
    }
}
