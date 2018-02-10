package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.service.CafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 2. 18..
 */
@RestController
@RequestMapping("api")
public class ApiCafeController {
    @Autowired
    private CafeService cafeService;

    @GetMapping("cafes/{cafeUrl}")
    public Cafe getCafeByUrl(@PathVariable String cafeUrl) {
        return cafeService.getCafe(cafeUrl);
    }
}