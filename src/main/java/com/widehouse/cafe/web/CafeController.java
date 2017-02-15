package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.service.CafeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RestController
@Slf4j
public class CafeController {
    @Autowired
    private CafeService cafeService;

    @GetMapping("/cafes/categories/{categoryId}")
    public List<Cafe> getCafesByCategory(@PathVariable Long categoryId) {
        return cafeService.getCafeByCategory(categoryId);
    }
}
