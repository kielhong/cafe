package com.widehouse.cafe.comment.service;

import static com.widehouse.cafe.cafe.entity.CafeVisibility.PUBLIC;
import static org.springframework.data.domain.Sort.Direction.ASC;

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

import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public Comment writeComment(Article article, User user, String commentContent) {
        if (!isCafeMember(article.getCafe(), user)) {
            throw new NoAuthorityException();
        }

        Comment comment = new Comment(article, user, commentContent);
        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public Comment writeReplyComment(Comment comment, User user, String commentText) {
        Cafe cafe = cafeRepository.findById(comment.getCafeId())
                .orElse(new Cafe());
        if (!isCafeMember(cafe, user)) {
            throw new NoAuthorityException();
        }

        comment.getComments().add(new Comment(comment.getCafeId(), comment.getArticleId(), user, commentText));
        Comment writeResult = commentRepository.save(comment);

        return writeResult.getComments().get(writeResult.getComments().size() - 1);
    }

    @Transactional
    public void modifyComment(Comment comment, User user, String newComment) {
        if (!isCommenter(comment, user)) {
            throw new NoAuthorityException();
        }
        comment.modify(newComment);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Comment comment, User user) {
        Cafe cafe = cafeRepository.findById(comment.getCafeId())
                .orElse(new Cafe());
        CafeMember cafeMember = cafeMemberRepository.findByCafeAndMember(cafe, user);
        if (!isCommenter(comment, user) && !isCafeManager(cafeMember)) {
            throw new NoAuthorityException();
        }

        commentRepository.delete(comment);
    // TODO - move to cafeService, articleService
//            cafe.getData().decreaseCommentCount();
//            cafeRepository.save(cafe);
//
//            article.decreaseCommentCount();
//            articleRepository.save(article);
    }

    public List<Comment> getComments(User user, Long articleId, int page, int size) {
        return commentRepository.findByArticleId(articleId, PageRequest.of(page, size, new Sort(ASC, "id")));
    }

    private boolean isCommentReadable(Cafe cafe, User user) {
        return cafe.getVisibility() == PUBLIC || isCafeMember(cafe, user);
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
