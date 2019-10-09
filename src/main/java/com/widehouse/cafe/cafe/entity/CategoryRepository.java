package com.widehouse.cafe.cafe.entity;

import com.widehouse.cafe.cafe.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kiel on 2017. 2. 19..
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
