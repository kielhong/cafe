package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.service.CafeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RestController
@Slf4j
public class CategoryController {
    @Autowired
    private CafeService cafeService;

    @GetMapping("/categories/{categoryId}/cafes")
    public List<Cafe> getCafesByCategory(@PathVariable Long categoryId,
                                         @PageableDefault(page = 0, size = 10,
                                                 direction = Sort.Direction.DESC,
                                                 sort = "statistics.cafeMemberCount") Pageable pageable) {
        return cafeService.getCafeByCategory(categoryId, pageable);
    }
}