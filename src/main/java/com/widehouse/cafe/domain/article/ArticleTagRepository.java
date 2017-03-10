package com.widehouse.cafe.domain.article;

import com.widehouse.cafe.domain.cafe.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by kiel on 2017. 3. 9..
 */
public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
    @Query("SELECT DISTINCT(at.tag) FROM ArticleTag at WHERE at.article.board.cafe = ?1")
    List<Tag> findAllByCafe(Cafe cafe);
}
