package com.widehouse.cafe.domain.article;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 12..
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByArticle(Article article, Pageable pageable);
}
