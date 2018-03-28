package com.widehouse.cafe.domain.article;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by kiel on 2017. 2. 12..
 */
public interface CommentRepository extends CrudRepository<Comment, String> {
    List<Comment> findByArticleId(Long articleId, Pageable pageable);
}
