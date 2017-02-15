package com.widehouse.cafe.domain.cafe;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
public interface CafeRepository extends JpaRepository<Cafe, Long> {
    List<Cafe> findByCategory(CafeCategory category);

    List<Cafe> findByCategory(CafeCategory category, Pageable pageable);

    List<Cafe> findByCategoryId(Long categoryId);

    List<Cafe> findByCategoryId(Long categoryId, Pageable pageable);
}
