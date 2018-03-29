package com.widehouse.cafe.domain.article;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CommentReactiveRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findByArticleId(Long articleId);
}
