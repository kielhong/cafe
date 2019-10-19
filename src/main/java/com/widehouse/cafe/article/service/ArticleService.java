package com.widehouse.cafe.article.service;

import static com.widehouse.cafe.cafe.entity.CafeVisibility.PUBLIC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.article.entity.Tag;
import com.widehouse.cafe.article.entity.TagRepository;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.common.event.ArticleCreateEvent;
import com.widehouse.cafe.common.exception.NoAuthorityException;
import com.widehouse.cafe.user.entity.User;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kiel on 2017. 2. 19..
 */
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CafeMemberRepository cafeMemberRepository;
    private final TagRepository tagRepository;
    private final ApplicationEventPublisher eventPublisher;

    public List<Article> getArticles(Cafe cafe, int page, int size) {
        return articleRepository.findByBoardCafe(cafe,
                PageRequest.of(page, size, Sort.by(DESC, "id")));
    }

    public List<Article> getArticles(Board board, int page, int size) {
        return articleRepository.findByBoard(board,
                PageRequest.of(page, size, Sort.by(DESC, "id")));
    }

    /**
     * get a article with reader.
     * if reader has no read auth then throw NoAuthorityException
     * @param id id of article
     * @param reader reader {@link User}
     */
    public Article getArticle(Long id, User reader) {
        Article article = articleRepository.findById(id).get();

        if (isArticleReadable(article.getCafe(), reader)) {
            return article;
        } else {
            throw new NoAuthorityException();
        }
    }

    /**
     * write a article.
     * @return created Article
     */
    public Article writeArticle(Board board, User writer, String title, String content) {
        if (!cafeMemberRepository.existsByCafeMember(board.getCafe(), writer)) {
            throw new NoAuthorityException();
        }

        Article article = articleRepository.save(new Article(board, writer, title, content));

        eventPublisher.publishEvent(new ArticleCreateEvent(board.getCafe()));

        return article;
    }

    /**
     * add tag to article.
     */
    @Transactional
    public void addTag(Article article, Tag tag) {
        article.getTags().add(tag);
        articleRepository.save(article);
        tag.getArticles().add(article);
        tagRepository.save(tag);
    }

    private boolean isArticleReadable(Cafe cafe, User reader) {
        if (cafe.getVisibility() == PUBLIC) {
            return true;
        } else {
            return cafeMemberRepository.existsByCafeMember(cafe, reader);
        }
    }

    /**
     * increase comment count of article.
     * @param id Article id
     */
    public void addComment(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article " + id + " does not exist"));

        article.increaseCommentCount();

        articleRepository.save(article);
    }

    /**
     * decrease comment count of article.
     * @param id Article id
     */

    public void removeComment(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article " + id + " does not exist"));

        article.decreaseCommentCount();

        articleRepository.save(article);
    }
}
