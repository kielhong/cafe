package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PRIVATE;
import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.article.CommentRepository;
import com.widehouse.cafe.domain.cafe.CafeMemberRepository;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
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
        if (cafe.isCafeMember(commenter)) {
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

    public void modifyComment(Comment comment, Member member, String newComment) {
        if (comment.getCommenter().equals(member)) {
            comment.modify(member, newComment);
        } else {
            throw new NoAuthorityException();
        }
    }

    @Transactional
    public void deleteComment(Long commentId, Member member) {
        Comment comment = commentRepository.findOne(commentId);
        Article article = comment.getArticle();
        Cafe cafe = comment.getArticle().getCafe();
        if (comment.getCommenter().equals(member) ||
                cafe.getCafeManager().getMember().equals(member)) {
            commentRepository.delete(comment);

            cafe.getStatistics().decreaseCommentCount();
            cafeRepository.save(cafe);

            article.decreaseCommentCount();
            articleRepository.save(article);
        } else {
            throw new NoAuthorityException();
        }
    }

    public List<Comment> getComments(Member member, Long articleId, int page, int size) {
        Article article = articleRepository.findOne(articleId);
        Cafe cafe = article.getCafe();
        List<Comment> comments;
        if (cafe != null && isCommentReadable(cafe, member)) {
            comments = commentRepository.findByArticle(article,
                    new PageRequest(page, size, new Sort(ASC, "id")));
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
