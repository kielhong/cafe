package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.CategoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RestController
@RequestMapping("api")
public class ApiCategoryController {
    @Autowired
    private CafeService cafeService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("categories")
    public List<Category> getCategories() {
        return categoryService.findAll("listOrder");
    }

    @GetMapping("categories/{categoryId}/cafes")
    public List<Cafe> getCafesByCategory(@PathVariable Long categoryId,
                                         @PageableDefault(direction = Sort.Direction.DESC,
                                                 sort = "statistics.cafeMemberCount") Pageable pageable) {
        List<Cafe> cafes = cafeService.getCafeByCategory(categoryId, pageable);

        return cafes;
    }
}