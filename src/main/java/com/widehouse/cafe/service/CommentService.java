package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.board.Article;
import com.widehouse.cafe.domain.board.Comment;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import org.springframework.stereotype.Service;

/**
 * Created by kiel on 2017. 2. 12..
 */
@Service
public class CommentService {

    public Comment writeComment(Article article, Member commenter, String commentContent) {
        if (isCafeMember(article.getCafe(), commenter)) {
            Comment comment = new Comment(article, commenter, commentContent);
            Cafe cafe = article.getCafe();
            cafe.getStatistics().increaseCommentCount();

            return comment;
        } else {
            throw new NoAuthorityException();
        }
    }

    public void modifyComment(Comment comment, Member commenter, String newComment) {
        comment.modify(commenter, newComment);
    }

    public boolean isCafeMember(Cafe cafe, Member member) {
        return cafe.getCafeMembers().stream()
                .anyMatch(x -> x.getMember().equals(member));
    }
}
