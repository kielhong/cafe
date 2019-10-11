package com.widehouse.cafe.cafe.service;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.cafe.entity.CategoryRepository;
import com.widehouse.cafe.cafe.service.CategoryService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CafeRepository cafeRepository;

    public static final String ORDER = "listOrder";

    public List<Category> findAll(String sort) {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, sort));
    }

    public List<Cafe> getCafeByCategory(Integer categoryId, Pageable pageable) {
        return cafeRepository.findByCategoryId(categoryId, pageable);
    }
}
