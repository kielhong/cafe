package com.widehouse.cafe.article.entity;

import com.widehouse.cafe.cafe.entity.Cafe;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by kiel on 2017. 3. 9..
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    @Query("SELECT DISTINCT(a.tags) FROM Article a WHERE a.board.cafe = ?1")
    List<Tag> findAllByCafe(Cafe cafe);

    @Query("SELECT DISTINCT(a) FROM Article a "
            + "WHERE a.board.cafe = :cafe AND :tag MEMBER OF a.tags")
    List<Article> findArticlesByCafeAndTag(@Param("cafe") Cafe cafe, @Param("tag") Tag tag);
}
