package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 20..
 */
@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping(value = "/articles/{articleId}/comments", params = {"page", "size"})
    public List<Comment> getComments(@PathVariable Long articleId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        // TODO : member 코드 처리
        Member member = new Member("member");
        List<Comment> comments = commentService.getComments(member, articleId, page, size);

        return comments;
    }

    @PostMapping("/articles/{articleId}/comments")
    public Comment write(@PathVariable Long articleId,
                         @RequestBody Comment input) {
        // TODO : member 코드 처리
        Member member = new Member("member");
        Article article = articleRepository.findOne(articleId);

        return commentService.writeComment(article, member, input.getComment());
    }
}