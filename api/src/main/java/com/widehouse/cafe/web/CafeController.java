package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.service.CafeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 2. 18..
 */
@RestController
@Slf4j
public class CafeController {
    @Autowired
    private CafeService cafeService;

    @GetMapping(name = "/cafes", params = "id")
    public Cafe getCafeById(@RequestParam Long id) {
        Cafe cafe = cafeService.getCafe(id);

        return cafe;
    }

    @GetMapping("/cafes/{cafeUrl}")
    public Cafe getCafeByUrl(@PathVariable String cafeUrl) {
        Cafe cafe = cafeService.getCafe(cafeUrl);

        return cafe;
    }
}
