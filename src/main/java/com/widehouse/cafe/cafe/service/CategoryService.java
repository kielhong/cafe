package com.widehouse.cafe.cafe.service;

import com.widehouse.cafe.cafe.entity.Category;

import java.util.List;

public interface CategoryService {
    String ORDER = "listOrder";

    List<Category> findAll(String sort);
}
