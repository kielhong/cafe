package com.widehouse.cafe.comment.controller;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;
import com.widehouse.cafe.comment.entity.Comment;
import com.widehouse.cafe.comment.entity.CommentRepository;
import com.widehouse.cafe.comment.service.CommentService;
import com.widehouse.cafe.common.annotation.CurrentMember;
import com.widehouse.cafe.member.entity.Member;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 2. 20..
 */
@RestController
@RequestMapping("api")
@Slf4j
public class ApiCommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * GET /api/articles/{articleId}/comments.
     * @param articleId id of Article
     * @param page page
     * @param size size
     * @param member current member
     * @return list of {@link Comment}
     */
    @GetMapping("/articles/{articleId}/comments")
    public List<Comment> getComments(@PathVariable Long articleId,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @CurrentMember Member member) {
        List<Comment> comments = commentService.getComments(member, articleId, page, size);

        return comments;
    }

    /**
     * POST /api/articles/{articleId}/comments.
     * Create comment on article
     * @param articleId id of article
     * @param input comment request form
     * @param member current member
     * @return created {@link Comment}
     */
    @PostMapping("/articles/{articleId}/comments")
    public Comment write(@PathVariable Long articleId,
                         @RequestBody CommentForm input,
                         @CurrentMember Member member) {
        Article article = articleRepository.findById(articleId).get();

        return commentService.writeComment(article, member, input.getComment());
    }

    /**
     * POST /api/comments/{commentId}/comments.
     * Create reply comment on comment
     * @param commentId id of comment
     * @param input request comment form
     * @param member current member
     * @return created reply {@link Comment}
     */
    @PostMapping("/comments/{commentId}/comments")
    public Comment writeReplyComment(@PathVariable String commentId,
                                     @RequestBody CommentForm input,
                                     @CurrentMember Member member) {
        Comment comment = commentRepository.findById(commentId).get();

        return commentService.writeReplyComment(comment, member, input.getComment());
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class CommentForm {
        private String comment;
    }
}
