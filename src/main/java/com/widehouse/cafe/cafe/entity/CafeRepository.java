package com.widehouse.cafe.cafe.entity;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by kiel on 2017. 2. 15..
 */
public interface CafeRepository extends CrudRepository<Cafe, Long> {
    Cafe findByUrl(String url);

    List<Cafe> findByCategory(Category category);

    @Cacheable("cafes_category")
    List<Cafe> findByCategory(Category category, Pageable pageable);

    List<Cafe> findByCategoryId(Integer categoryId);

    @Cacheable("cafes_category")
    List<Cafe> findByCategoryId(Integer categoryId, Pageable pageable);
}
