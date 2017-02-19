package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.article.CommentRepository;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by kiel on 2017. 2. 12..
 */
@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private ArticleRepository articleRepository;

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
    public void deleteComment(Comment comment, Member member) {
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
}
