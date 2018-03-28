package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.cafe.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll(String sort);
}
