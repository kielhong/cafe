package com.widehouse.cafe.cafe.controller;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.cafe.service.CafeService;
import com.widehouse.cafe.cafe.service.CategoryService;

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
        return categoryService.findAll(CategoryService.ORDER);
    }

    @GetMapping("categories/{categoryId}/cafes")
    public List<Cafe> getCafesByCategory(@PathVariable Integer categoryId,
                                         @PageableDefault(direction = Sort.Direction.DESC,
                                                 sort = "statistics.cafeMemberCount") Pageable pageable) {
        List<Cafe> cafes = cafeService.getCafeByCategory(categoryId, pageable);

        return cafes;
    }
}