package com.widehouse.cafe.api;

import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafe.CategoryRepository;
import com.widehouse.cafe.projection.CafeProjection;
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
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.findAll(new Sort(ASC, "listOrder"));

        return categories;
    }

    @GetMapping("/categories/{categoryId}/cafes")
    public List<CafeProjection> getCafesByCategory(@PathVariable Long categoryId,
                                                   @PageableDefault(page = 0, size = 10,
                                                 direction = Sort.Direction.DESC,
                                                 sort = "statistics.cafeMemberCount") Pageable pageable) {
        List<CafeProjection> cafes = cafeService.getCafeByCategory(categoryId, pageable);

        return cafes;
    }
}