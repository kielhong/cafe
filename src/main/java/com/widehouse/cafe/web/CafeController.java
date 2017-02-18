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

    @GetMapping("/cafes/?id={cafeId}")
    public Cafe getCafe(@RequestParam Long cafeId) {
        Cafe cafe = cafeService.getCafe(cafeId);

        return cafe;
    }

    @GetMapping("/cafes/{cafeUrl}")
    public Cafe getCafe(@PathVariable String cafeUrl) {
        Cafe cafe = cafeService.getCafe(cafeUrl);

        return cafe;
    }
}
