package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import com.widehouse.cafe.projection.ArticleProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 19..
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CafeMemberRepository cafeMemberRepository;

    public List<ArticleProjection> getArticlesByCafe(Cafe cafe, int page, int size) {
        List<ArticleProjection> articles = articleRepository.findByCafe(cafe,
                new PageRequest(page, size, new Sort(DESC, "id")));

        return articles;
    }

    public List<Article> getArticlesByBoard(Board board, int page, int size) {
        List<Article> articles = articleRepository.findByBoard(board,
                new PageRequest(page, size, new Sort(DESC, "id")));

        return articles;
    }

    public Article getArticle(Long id, Member reader) {
        Article article = articleRepository.findOne(id);

        if (isArticleReadable(article.getCafe(), reader)) {
            return article;
        } else {
            throw new NoAuthorityException();
        }
    }

    private boolean isArticleReadable(Cafe cafe, Member reader) {
        if (cafe.getVisibility() == PUBLIC) {
            return true;
        } else {
            return cafeMemberRepository.existsByCafeMember(cafe, reader);
        }
    }
}
