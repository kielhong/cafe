package com.widehouse.cafe.api;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.article.CommentRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;
import com.widehouse.cafe.service.CommentService;
import com.widehouse.cafe.service.MemberDetailsService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberDetailsService memberDetailsService;

    @GetMapping("/articles/{articleId}/comments")
    public List<Comment> getComments(@PathVariable Long articleId,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        Member member = memberDetailsService.getCurrentMember();

        List<Comment> comments = commentService.getComments(member, articleId, page, size);

        return comments;
    }

    @PostMapping("/articles/{articleId}/comments")
    public Comment write(@PathVariable Long articleId,
                         @RequestBody CommentForm input) {
        Member member = memberDetailsService.getCurrentMember();
        Article article = articleRepository.findById(articleId).get();

        return commentService.writeComment(article, member, input.getComment());
    }

    @PostMapping("/comments/{commentId}/comments")
    public Comment writeReplyComment(@PathVariable String commentId,
                                     @RequestBody CommentForm input) {
        Member member = memberDetailsService.getCurrentMember();
        Comment comment = commentRepository.findById(commentId).get();
        Comment subComment = commentService.writeReplyComment(comment, member, input.getComment());

        return subComment;
    }

    @AllArgsConstructor
    @Getter
    public static class CommentForm {
        private String comment;
    }
}
