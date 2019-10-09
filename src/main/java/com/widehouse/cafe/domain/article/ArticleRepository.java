package com.widehouse.cafe.domain.article;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.cafe.entity.Cafe;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by kiel on 2017. 2. 18..
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByBoardCafe(@Param("cafe") Cafe cafe, Pageable pageable);

    List<Article> findByBoard(Board board, Pageable pageable);
}
