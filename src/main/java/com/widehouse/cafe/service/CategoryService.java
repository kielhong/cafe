package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.cafe.Category;

import java.util.List;

public interface CategoryService {
    String ORDER = "listOrder";

    List<Category> findAll(String sort);
}
