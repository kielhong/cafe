package com.widehouse.cafe.domain.article;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 12..
 */
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByArticleId(Long articleId, Pageable pageable);
}
