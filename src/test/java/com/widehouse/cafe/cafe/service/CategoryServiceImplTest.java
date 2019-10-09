package com.widehouse.cafe.cafe.service;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.cafe.entity.CategoryRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CategoryServiceImpl.class)
public class CategoryServiceImplTest {
    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    private List<Category> categories;

    @BeforeEach
    public void setUp() {
        categories = new ArrayList<>();
        IntStream.range(1, 11)
                .forEach(i -> categories.add(new Category(i, "category" + i, i, now())));
        given(categoryRepository.findAll(Sort.by(ASC, "listOrder")))
                .willReturn(categories.stream()
                        .sorted(Comparator.comparing(Category::getListOrder)).collect(Collectors.toList()));
    }


    @Test
    public void listCategories_thenListAllCategory() {
        List<Category> result = categoryService.findAll("listOrder");

        then(result)
                .isEqualTo(categories.stream()
                        .sorted(Comparator.comparing(Category::getListOrder)).collect(Collectors.toList()));
    }

}
