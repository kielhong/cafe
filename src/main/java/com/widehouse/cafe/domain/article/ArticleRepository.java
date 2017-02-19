package com.widehouse.cafe.domain.article;

import com.widehouse.cafe.domain.cafe.Cafe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 18..
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCafe(Cafe cafe, Pageable pageable);
}
