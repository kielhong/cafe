package com.widehouse.cafe.domain.article;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kiel on 2017. 2. 18..
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
