package com.widehouse.cafe.domain.article;

import com.widehouse.cafe.domain.cafe.Cafe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by kiel on 2017. 3. 9..
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    @Query("SELECT DISTINCT(a.tags) FROM Article a WHERE a.board.cafe = ?1")
    List<Tag> findAllByCafe(Cafe cafe);

    @Query("SELECT DISTINCT(a) FROM Article a " +
            "WHERE a.board.cafe = :cafe AND :tag MEMBER OF a.tags")
    List<Article> findArticlesByCafeAndTag(@Param("cafe") Cafe cafe, @Param("tag") Tag tag);
}
