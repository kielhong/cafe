package com.widehouse.cafe.comment.service;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMember;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.cafe.entity.CafeMemberRole;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.comment.entity.Comment;
import com.widehouse.cafe.comment.entity.CommentRepository;
import com.widehouse.cafe.common.exception.NoAuthorityException;
import com.widehouse.cafe.user.entity.User;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by kiel on 2017. 2. 12..
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CafeRepository cafeRepository;
    private final CafeMemberRepository cafeMemberRepository;

    /**
     * write a comment.
     * cafe member 일 경우에만 작성 가능
     * @return 생성된 comment
     */
    @Transactional
    public Comment writeComment(Article article, User user, String text) {
        if (!isCafeMember(article.getCafe(), user)) {
            throw new NoAuthorityException();
        }

        Comment comment = new Comment(article, user, text);
        commentRepository.save(comment);

        return comment;
    }

    /**
     * reply to a comment.
     * @param comment reply 를 하려는 comment
     * @param user reply 작성 user
     * @param text reply comment text
     * @return 신규로 작성된 reply
     */
    @Transactional
    public Comment writeReplyComment(Comment comment, User user, String text) {
        Cafe cafe = cafeRepository.findById(comment.getCafeId())
                .orElse(new Cafe());
        if (!isCafeMember(cafe, user)) {
            throw new NoAuthorityException();
        }

        comment.getReplies().add(new Comment(comment.getCafeId(), comment.getArticleId(), user, text));
        Comment writeResult = commentRepository.save(comment);

        return writeResult.getReplies().get(writeResult.getReplies().size() - 1);
    }

    /**
     * modify a comment.
     * comment 작성자일 경우에만 삭제 가능
     */
    @Transactional
    public void modifyComment(Comment comment, User user, String text) {
        if (!isCommenter(comment, user)) {
            throw new NoAuthorityException();
        }
        comment.modify(text);
        commentRepository.save(comment);
    }

    /**
     * delete a comment.
     * comment 작성자이거나 cafe manager 이면 삭제 가능
     * @param comment 삭제하려는 Comment
     * @param user 삭제하려는 user
     */
    @Transactional
    public void deleteComment(Comment comment, User user) {
        Cafe cafe = cafeRepository.findById(comment.getCafeId())
                .orElse(new Cafe());
        CafeMember cafeMember = cafeMemberRepository.findByCafeAndMember(cafe, user);
        if (!isCommenter(comment, user) && !isCafeManager(cafeMember)) {
            throw new NoAuthorityException();
        }

        commentRepository.delete(comment);
    }

    public Comment getComment(String commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean isCafeMember(Cafe cafe, User user) {
        return cafeMemberRepository.existsByCafeMember(cafe, user);
    }

    private boolean isCommenter(Comment comment, User user) {
        return comment.getMember().getId().equals(user.getId());
    }

    private boolean isCafeManager(CafeMember cafeMember) {
        return cafeMember.getRole() == CafeMemberRole.MANAGER;
    }


}
