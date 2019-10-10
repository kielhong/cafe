package com.widehouse.cafe.article.service;

import static com.widehouse.cafe.cafe.entity.CafeVisibility.PUBLIC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;
import com.widehouse.cafe.article.entity.Tag;
import com.widehouse.cafe.article.entity.TagRepository;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.member.entity.Member;
import com.widehouse.cafe.common.event.ArticleCreateEvent;
import com.widehouse.cafe.common.exception.NoAuthorityException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kiel on 2017. 2. 19..
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CafeMemberRepository cafeMemberRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;


    public List<Article> getArticlesByCafe(Cafe cafe, int page, int size) {
        return articleRepository.findByBoardCafe(cafe,
                PageRequest.of(page, size, new Sort(DESC, "id")));
    }

    public List<Article> getArticlesByBoard(Board board, int page, int size) {
        List<Article> articles = articleRepository.findByBoard(board,
                PageRequest.of(page, size, new Sort(DESC, "id")));

        return articles;
    }

    public Article getArticle(Long id, Member reader) {
        Article article = articleRepository.findById(id).get();

        if (isArticleReadable(article.getCafe(), reader)) {
            return article;
        } else {
            throw new NoAuthorityException();
        }
    }

    public Article writeArticle(Board board, Member writer, String title, String content) {
        if (!cafeMemberRepository.existsByCafeMember(board.getCafe(), writer)) {
            throw new NoAuthorityException();
        }

        Article article = articleRepository.save(new Article(board, writer, title, content));

        eventPublisher.publishEvent(new ArticleCreateEvent(board.getCafe()));

        return article;
    }

    @Transactional
    public void addTag(Article article, Tag tag) {
        article.getTags().add(tag);
        articleRepository.save(article);
        tag.getArticles().add(article);
        tagRepository.save(tag);
    }

    private boolean isArticleReadable(Cafe cafe, Member reader) {
        if (cafe.getVisibility() == PUBLIC) {
            return true;
        } else {
            return cafeMemberRepository.existsByCafeMember(cafe, reader);
        }
    }
}
