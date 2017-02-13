package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.board.Article;
import com.widehouse.cafe.domain.board.Comment;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CommentRepository;
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
    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment writeComment(Article article, Member commenter, String commentContent) {
        Cafe cafe = article.getCafe();
        if (cafe.isCafeMember(commenter)) {
            Comment comment = new Comment(article, commenter, commentContent);
            commentRepository.save(comment);

            cafe.getStatistics().increaseCommentCount();

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
        Cafe cafe = comment.getArticle().getCafe();
        if (comment.getCommenter().equals(member) ||
                cafe.getCafeManager().getMember().equals(member)) {
            commentRepository.delete(comment);

            cafe.getStatistics().decreaseCommentCount();
        } else {
            throw new NoAuthorityException();
        }
    }
}
