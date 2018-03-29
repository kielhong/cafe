package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.article.CommentReactiveRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api")
public class ApiCommentStreamController {
    @Autowired
    private CommentReactiveRepository commentRepository;

    @GetMapping("/articles/{articleId}/stream/comments")
    public Flux<Comment> getAllComments(@PathVariable Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }
}
