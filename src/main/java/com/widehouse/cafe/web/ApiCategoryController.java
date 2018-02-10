package com.widehouse.cafe.web;

import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafe.CategoryRepository;
import com.widehouse.cafe.service.CafeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private CategoryRepository categoryRepository;

    @GetMapping("categories")
    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.findAll(new Sort(ASC, "listOrder"));

        return categories;
    }

    @GetMapping("categories/{categoryId}/cafes")
    public List<Cafe> getCafesByCategory(@PathVariable Long categoryId,
                                         @PageableDefault(direction = Sort.Direction.DESC,
                                                 sort = "statistics.cafeMemberCount") Pageable pageable,
                                         @RequestParam(defaultValue = "${default.age}") Integer age) {
        System.out.println("age = " + age);
        List<Cafe> cafes = cafeService.getCafeByCategory(categoryId, pageable);

        return cafes;
    }
}