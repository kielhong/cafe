package com.widehouse.cafe.cafe.service;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.cafe.entity.CategoryRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private CategoryService service;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CafeRepository cafeRepository;

    @BeforeEach
    void setUp() {
        service = new CategoryService(categoryRepository, cafeRepository);
    }

    @Test
    void listCategories_thenListAllCategory() {
        // given
        List<Category> categories = new ArrayList<>();
        IntStream.range(1, 11)
                .forEach(i -> categories.add(new Category(i, "category" + i, i, now())));
        given(categoryRepository.findAll(Sort.by(ASC, "listOrder")))
                .willReturn(categories.stream()
                        .sorted(Comparator.comparing(Category::getListOrder)).collect(Collectors.toList()));
        // when
        List<Category> result = service.findAll(CategoryService.ORDER);
        // then
        then(result)
                .isEqualTo(categories.stream()
                        .sorted(Comparator.comparing(Category::getListOrder)).collect(Collectors.toList()));
    }

    @Test
    void getCafesByCategory_should_return_cafes_by_category() {
        // given
        Cafe cafe1 = new Cafe();
        Cafe cafe2 = new Cafe();
        Cafe cafe3 = new Cafe();
        Cafe cafe4 = new Cafe();
        Category category = new Category(1, "category", 1, now());
        given(cafeRepository.findByCategoryId(category.getId(),
                PageRequest.of(0, 4, Sort.by(DESC, "statistics.cafeMemberCount"))))
                .willReturn(Arrays.asList(cafe4, cafe3, cafe2, cafe1));
        // then
        List<Cafe> cafes = service.getCafeByCategory(category.getId(),
                PageRequest.of(0, 4, Sort.by(DESC, "statistics.cafeMemberCount")));

        then(cafes)
                .contains(cafe4, cafe3, cafe2, cafe1);
    }
}
