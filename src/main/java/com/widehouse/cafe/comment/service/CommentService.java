package com.widehouse.cafe.comment.service;

import static com.widehouse.cafe.cafe.entity.CafeVisibility.PRIVATE;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;
import com.widehouse.cafe.comment.entity.Comment;
import com.widehouse.cafe.comment.entity.CommentRepository;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.cafe.entity.CafeMember;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.cafe.entity.CafeMemberRole;
import com.widehouse.cafe.member.entity.Member;
import com.widehouse.cafe.common.exception.NoAuthorityException;
import com.widehouse.cafe.cafe.service.CafeMemberService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by kiel on 2017. 2. 12..
 */
@Service
@Slf4j
public class CommentService {
    @Autowired
    private CafeMemberService cafeMemberService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private CafeMemberRepository cafeMemberRepository;

    @Transactional
    public Comment writeComment(Article article, Member commenter, String commentContent) {
        Cafe cafe = article.getCafe();
        if (cafeMemberService.isCafeMember(cafe, commenter)) {
            Comment comment = new Comment(article, commenter, commentContent);
            commentRepository.save(comment);

            cafe.getData().increaseCommentCount();
            cafeRepository.save(cafe);

            article.increaseCommentCount();
            articleRepository.save(article);

            return comment;
        } else {
            throw new NoAuthorityException();
        }
    }

    @Transactional
    public Comment writeReplyComment(Comment comment, Member commenter, String commentText) {
        Optional<Article> article = articleRepository.findById(comment.getArticleId());
        Cafe cafe = article.get().getCafe();
        if (cafeMemberService.isCafeMember(cafe, commenter)) {
            comment.getComments().add(new Comment(comment.getArticleId(), commenter, commentText));
            Comment writeResult = commentRepository.save(comment);

            return writeResult.getComments().get(writeResult.getComments().size() - 1);
        } else {
            throw new NoAuthorityException();
        }
    }

    @Transactional
    public void modifyComment(Comment comment, Member member, String newComment) {
        if (comment.getMember().getId().equals(member.getId())) {
            comment.modify(member, newComment);
            commentRepository.save(comment);
        } else {
            throw new NoAuthorityException();
        }
    }

    @Transactional
    public void deleteComment(String commentId, Member deleter) {
        Comment comment = commentRepository.findById(commentId).get();
        Article article = articleRepository.findById(comment.getArticleId()).get();
        Cafe cafe = article.getCafe();
        CafeMember cafeMember = cafeMemberRepository.findByCafeAndMember(cafe, deleter);
        if (comment.getMember().getId().equals(deleter.getId())
                || cafeMember.getRole() == CafeMemberRole.MANAGER) {
            commentRepository.delete(comment);

            cafe.getData().decreaseCommentCount();
            cafeRepository.save(cafe);

            article.decreaseCommentCount();
            articleRepository.save(article);
        } else {
            throw new NoAuthorityException();
        }
    }

    public List<Comment> getComments(Member member, Long articleId, int page, int size) {
        Article article = articleRepository.findById(articleId).get();
        Cafe cafe = article.getCafe();
        List<Comment> comments;
        if (cafe != null && isCommentReadable(cafe, member)) {
            comments = commentRepository.findByArticleId(articleId, PageRequest.of(page, size, new Sort(ASC, "id")));
        } else {
            comments = Collections.emptyList();
        }

        return comments;
    }

    private boolean isCommentReadable(Cafe cafe, Member member) {
        if (cafe.getVisibility() == PRIVATE) {
            return cafeMemberService.isCafeMember(cafe, member);
        } else {
            return true;
        }
    }
}
