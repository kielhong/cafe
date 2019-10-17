package com.widehouse.cafe.comment.service;

import static com.widehouse.cafe.cafe.entity.CafeVisibility.PUBLIC;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.comment.entity.Comment;
import com.widehouse.cafe.comment.entity.CommentRepository;
import com.widehouse.cafe.user.entity.User;

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentListService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final CafeMemberRepository cafeMemberRepository;

    /**
     * article 에 달린 comment 를 paging 으로 반환한다.
     * cafe member 혹은 public cafe 일 경우에는 comment list를 , 아닐 경우 empty list 를 반환한다.
     * @param user comment 를 보려는 사용자.
     */
    public List<Comment> listComments(User user, Long articleId, int page, int size) {
        Cafe cafe = articleRepository.findById(articleId)
                .map(Article::getCafe)
                .orElse(new Cafe());
        if (isCommentReadable(cafe, user)) {
            return commentRepository.findByArticleId(articleId, PageRequest.of(page, size, new Sort(ASC, "id")));
        }

        return Collections.emptyList();
    }

    private boolean isCommentReadable(Cafe cafe, User user) {
        return cafe.getVisibility() == PUBLIC || isCafeMember(cafe, user);
    }

    private boolean isCafeMember(Cafe cafe, User user) {
        return cafeMemberRepository.existsByCafeMember(cafe, user);
    }
}
