package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PRIVATE;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.article.CommentRepository;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafemember.CafeMemberRole;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;

/**
 * Created by kiel on 2017. 2. 12..
 */
@Service
@Slf4j
public class CommentService {
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
        if (cafeMemberRepository.existsByCafeMember(cafe, commenter)) {
            Comment comment = new Comment(article, commenter, commentContent);
            commentRepository.save(comment);

            cafe.getStatistics().increaseCommentCount();
            cafeRepository.save(cafe);

            article.increaseCommentCount();
            articleRepository.save(article);

            return comment;
        } else {
            throw new NoAuthorityException();
        }
    }

    @Transactional
    public Comment writeSubComment(Comment comment, Member commenter, String commentText) {
        comment.getComments().add(new Comment(comment.getArticleId(), commenter, commentText));
        commentRepository.save(comment);

        return comment.getComments().get(comment.getComments().size() - 1);
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
        Comment comment = commentRepository.findOne(commentId);
        Article article = articleRepository.findOne(comment.getArticleId());
        Cafe cafe = article.getCafe();
        CafeMember cafeMember = cafeMemberRepository.findByCafeAndMember(cafe, deleter);
        if (comment.getMember().getId().equals(deleter.getId())
                || cafeMember.getRole() == CafeMemberRole.MANAGER) {
            commentRepository.delete(comment);

            cafe.getStatistics().decreaseCommentCount();
            cafeRepository.save(cafe);

            article.decreaseCommentCount();
            articleRepository.save(article);
        } else {
            throw new NoAuthorityException();
        }
    }

    public Comment getComment(String id) {
        return null;
    }

    public List<Comment> getComments(Member member, Long articleId, int page, int size) {
        Article article = articleRepository.findOne(articleId);
        Cafe cafe = article.getCafe();
        List<Comment> comments;
        if (cafe != null && isCommentReadable(cafe, member)) {
            comments = commentRepository.findByArticleId(articleId, new PageRequest(page, size, new Sort(ASC, "id")));
        } else {
            comments = Collections.emptyList();
        }

        return comments;
    }

    private boolean isCommentReadable(Cafe cafe, Member member) {
        if (cafe.getVisibility() == PRIVATE) {
            return cafeMemberRepository.existsByCafeMember(cafe, member);
        } else {
            return true;
        }
    }
}
